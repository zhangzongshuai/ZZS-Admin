/*
    系统管理模块
 */

let userApi = '/api/userManager/'
let menuApi = '/api/menuManager/'
let roleApi = '/api/roleManager/'
let iconApi = "/api/iconManager/"

export const systemApi = {

    //user用户管理
    getUsers: userApi + 'pageList',

    modifyPassworld: userApi + 'modifyPassword',

    resetPwd: userApi + 'resetPassword',

    deleteUser: userApi + 'deleteUser',

    addUser: userApi + 'addUser',

    getUser: userApi + 'user',

    modifyUser: userApi + 'modifyUser',

    configUserRole : userApi + 'configUserRoles',

    getUserRoles : userApi + 'rolesByUserId',

    downloadUsers:userApi + 'downloadUsers',


    //菜单管理
    getMenus: menuApi + 'pageList',

    addMenu: menuApi + 'add',

    deleteMenu: menuApi + 'delete',

    modifyMenu: menuApi + 'modify',

    getMenuTree: menuApi + 'menuTree',

    getMenu: menuApi + 'menu',

    getUserMenu: menuApi + 'userMenu',

    getUserBtn: menuApi + 'userBtn',

    // 图标管理
    getIconList: iconApi + 'iconList',

    //角色管理

    getRoles: roleApi + "pageList",

    getRole: roleApi + "role",

    deleteRole: roleApi + 'delete',

    modifyRole: roleApi + 'modifyRole',

    addRole: roleApi + 'add',

    getRoleMenus: roleApi + 'roleMenus',

    configRoleMenu: roleApi + 'configRoleMenu',

    getRoleUsers: roleApi + 'usersByRoleId',

    configRoleUser: roleApi + 'configRoleUser',


}
