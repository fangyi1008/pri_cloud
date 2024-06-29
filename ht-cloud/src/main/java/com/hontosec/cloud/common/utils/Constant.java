/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.common.utils;
/**
 * 常量
 * @author fangyi
 *
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
	  /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     * 升序
     */
    public static final String ASC = "asc";
    
    /**
	 * http请求
	 */
	public static final String HTTP = "http://";

	/**
	 * https请求
	 */
	public static final String HTTPS = "https://";
	/**
	 * 资源映射路径 前缀
	 */
	public static final String RESOURCE_PREFIX = "/profile";
	/**
	 * kvm连接协议类型
	 */
	public static final String KVM_TCP = "tcp";
	public static final String KVM_SSH = "ssh";
	public static final String KVM_TLS = "tls";
	
	/**
	 * 数据库加密盐值
	 */
	public static String JASYPT_ENCRYPTOR_VALUE="hontosec_cloud_db";
	
	/**
	 * 存储卷基础路径
	 */
	public static String STORAGE_VOLUME_BASE_PATH = "/htcloud/volume/";

	/**
	 * 存储卷默认类型
	 */
	public static String FILE_SYSTEM = "qcow2";

	/**
	 * 存储卷容量
	 */
	public static String STORAGE_VOLUME_CAPACITY = "80G";

	/**
	 * 存储卷基础路径
	 */
	public static String STORAGE_TYPE = "1";

	/**
	 * 存储卷基础路径
	 */
	public static String CREATE_FORMAT = "qcow2";

	/**
	 * 存储池默认ID-当创建虚拟机时没有选择存储池时
	 */
	public static String STORAGE_POOL_ID = "1547858802909401089";

	/**
	 * 虚拟机模板基础路径
	 */
	public static String VM_TEMPLATE_PATH = "/htcloud/";
	/**
	 * AES key
	 */
	public static String AES_KEY = "404142434445464748494A4B4C4D4E4C";
	/**
	 * SM4 root key
	 */
	public static String SM4_ROOT_KEY = "ADC31AB73DCB1EE23AE7DA75942404E3";
	/**
	 * SM4 lmk key
	 */
	public static String SM4_LMK_KEY = "2D1D96FBAC78C5BDC4E287E2CCB6C2CD";
	/**
	 * 集群资源统计配置code
	 */
	public static String CLUSTER_MONITOR_CODE = "cluster_monitor_code";
	/**
	 * 主机资源统计配置code
	 */
	public static String HOST_MONITOR_CODE = "host_monitor_code";
	/**
	 * ip分配统计配置code
	 */
	public static String IP_MONITOR_CODE = "ip_monitor_code";
	/**
	 * 存储资源统计配置code
	 */
	public static String STORAGE_MONITOR_CODE = "storage_monitor_code";
	/**
	 * vlan分配统计配置code
	 */
	public static String VLAN_MONITOR_CODE = "vlan_monitor_code";
	/**
	 * 虚拟机资源统计配置code
	 */
	public static String VM_MONITOR_CODE = "vm_monitor_code";
	/**
	 * 主机状态同步code
	 */
	public static final String HOST_STATUS_SYNC_CODE = "host_status_sync_code";
	
	/**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
