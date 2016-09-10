# GreenDaoDemo
使用GreenDao ORM 框架进行数据库操作,并对其进行了一些封装
##GreenDao框架的使用步骤:(android studio)
###1.首先在project的build.gradle下进行添加如下依赖:

	dependencies {
        classpath 'org.greenrobot:greendao-gradle-plugin:3.1.1'
      }
###2.然后在Module的build.gradle下添加如下:
	 sourceSets{
        main{
            java.srcDirs=['src/main/java','src/main/java-gen']
        }
    }

	dependencies {
    compile 'org.greenrobot:greendao:3.1.1'
    compile 'org.greenrobot:greendao-generator:3.1.0'
	}
###3.依赖模块加载完毕之后,添加greendao generator模块
		1.在 .src/main 目录下新建一个与 java 同层级的「java-gen」目录，用于存放由
	 greenDAO 生成的 Bean、DAO、DaoMaster、DaoSession 等类。
		2.然后在project中新添加一个纯java模块,也就是一个java library
		3.在新创建的java Module中添加依赖:
			dependencies {
    			compile 'org.greenrobot:greendao-generator:3.1.0'
				}
###4.在新建的模块中唯一的java类中编写,要创建的数据库
		public static void main(String[] args) {
        //创建一个用于添加实体（Entity）的模式（Schema）对象
        // 两个参数分别代表：数据库版本号 和 自动生成实体类的包路径。
        Schema schema = new Schema(1, "com.student.entity");
        //设置自动生成的Dao类所在的包
        schema.setDefaultJavaPackageDao("com.student.dao");
        
                     //  模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以
                    //  及是否使用 keep sections。         
                    // schema2.enableActiveEntitiesByDefault();         
                     // schema2.enableKeepSectionsByDefault();
        
        //向Schema对象(数据库)中添加实体类(表)
        addStudent(schema);
        //最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，
        //此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        try {
            new DaoGenerator().generateAll(schema, "E:\\AndroidWorkSpace\\GreenDaoDemo\\app\\src\\main\\java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建数据库的表
    private static void addStudent(Schema schema) {
        Entity entity = schema.addEntity("Student");//创建数据库的表
        //对数据库进行重新命名
        //entity.setTableName("tableName");
        
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。         
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        entity.addIdProperty();//主键 是int类型
        entity.addStringProperty("name");
        entity.addStringProperty("address");
        entity.addIntProperty("age");
    }
###4.执行添加的Moudule的main方法,将在设置好的java-gen目录下生成对应的代码.
###5.然后就在安卓工程中进行数据库操作吧
###6.首先定义一个单例manager用于得到对数据库进行操作的Session对象.
	/**
	 * 1.创建数据库
	 * 2.创建表
	 * 3.增删改查
	 * 4.对数据库的升级
	 * Created by ZheWei on 2016/9/9.
	 */
	public class DaoManager {
	    private static final String TAG = "DaoManager";
	    private static final String DB_NAME = "mydb.sqlite";//数据库名称
	
	    private volatile static DaoManager manager;//多线程访问声明为单例模式
	
	    private static DaoMaster.DevOpenHelper helper;
	    private static DaoMaster master;
	    private static DaoSession session;
	
	    private Context mContext;
	
	    private DaoManager() {
	
	    }
	
	    public void init(Context context) {
	        this.mContext = context;
	    }
	
	    public static DaoManager getInstance() {
	        if (manager == null) {
	            synchronized (DaoManager.class) {
	                if (manager == null) {
	                    manager = new DaoManager(); 
	                }
	            }
	        }
	        return manager;
	    }
	
	    /**
	     * 判断是否存在数据库,如果没有则创建数据库
	     */
	    // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
	    // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
	    // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
	    // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
	    public DaoMaster getMaster() {
	        if (master == null) {
	            helper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
	            master = new DaoMaster(helper.getWritableDatabase());
	        }
	        return master;
	    }
	
	    /**
	     * 完成对数据库的 增删改查 ,这里仅仅是一个接口
	     */
	    public DaoSession getSession() {
	        if (session == null) {
	            if (master == null) {
	                master = getMaster();
	            }
	            session = master.newSession();
	        }
	        return session;
	    }
	
	    /**
	     * 打开输出日志的操作,默认的是关闭的
	     */
	    public void setDeBug() {
	        QueryBuilder.LOG_SQL = true;
	        QueryBuilder.LOG_VALUES = true;
	    }
	
	    /**
	     * 关闭所有的连接
	     */
	    public void closeConnections() {
	        closeHelper();
	        closeSession();
	    }
	
	    private void closeSession() {
	        if (session != null) {
	            session.clear();
	            session = null;
	        }
	    }
	
	    private void closeHelper() {
	        if (helper != null) {
	            helper.close();
	            helper = null;
	        }
	    }
	
	}
##7.定义一个工具类用于具体的对数据库进行增删改查操作
	/**
	 * 完成对某一张表的具体操作, ORM 操作的是对象
	 * Created by ZheWei on 2016/9/9.
	 */
	public class CommonUtils {
    private DaoManager mDaoManager;

    public CommonUtils(Context context) {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.init(context);
    }

    /**
     * 完成对数据库表的插入操作-->并且会检测数据库是否存在,不存在自己创建,
     */
    public boolean insertStudent(Student student) {
        boolean flag;
        flag = mDaoManager.getSession().insert(student) != -1;//不等于-1是true 否则是false
        Log.i("MainActivity", "insertStudent: " + flag);
        return flag;
    }

    /**
     * 同时插入多条记录
     */
    public boolean insertMultStudent(final List<Student> students) {
        boolean flag = false;
        try {
            mDaoManager.getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Student s : students) {
                        mDaoManager.getSession().insertOrReplace(s);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("MainActivity", "insertMultStudent: " + flag);
        return flag;
    }

    /**
     * 修改指定记录
     */
    public boolean uoDateStudent(Student student) {
        boolean flag = false;
        try {
            mDaoManager.getSession().update(student);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("MainActivity", "uoDateStudent: " + flag);
        return flag;
    }

    /**
     * 删除指定记录
     */
    public boolean deleteStudent(Student student) {
        boolean flag = false;
        try {
            mDaoManager.getSession().delete(student);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("MainActivity", "deleteStudent: " + flag);
        return flag;
    }

    /**
     * 删除所有的记录
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            mDaoManager.getSession().deleteAll(Student.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("aaa", "deleteAll: " + flag);
        return flag;
    }

    /**
     * 查询 某一个表 的 所有记录
     */
    public List<Student> listAll() {
        return mDaoManager.getSession().loadAll(Student.class);
    }

    /**
     * 按照主键查询某一个 表 中 的单行记录
     */
    public Student listOneStudent(long key) {
        return mDaoManager.getSession().load(Student.class, key);
    }

    /**
     * 按照sql语句进行查询
     */
    public void queryBySql() {
        List<Student> list = mDaoManager.getSession().queryRaw(Student.class, "where name like ? and _id<=?", new String[]{"%jo%", "4"});
        for (Student s : list) {
            Log.i("MainActivity", s.getId() + "");
        }
    }

    /**
     * 使用查询构建器进行查询
     */
    public void queryByBuilder() {
        //使用查询构建器
        QueryBuilder<Student> queryBuilder = mDaoManager.getSession().queryBuilder(Student.class);
        //这些条件是 逻辑与
        queryBuilder.where(StudentDao.Properties.Name.like("john"));
        List<Student> list = queryBuilder.where(StudentDao.Properties.Id.le(4)).list();
        for (Student s : list) {
            Log.i("MainActivity", s.getId() + "");
        }
    }

	}
