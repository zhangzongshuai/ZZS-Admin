/*
    系统管理模块
 */

let oilApi = '/api/oil/'
let userApi = '/api/userManager/'
let oilTypeApi = '/api/oiltype/'
let stationApi = '/api/station/'
let menuApi = '/api/menuManager/'
let roleApi = '/api/roleManager/'
let provinceApi = '/api/province/'

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

    //station 车站管理
    getStationList: stationApi + "list",

    getStationPage :stationApi + 'page',

    deleteStation: stationApi + "delete",

    addStation: stationApi + "add",

    modifyStation: stationApi + "modify",

    //oil 油品类别管理
    getOilTypeTree: oilTypeApi + 'treelist',

    getOilTypeList: oilTypeApi + 'list',

    addOilType: oilTypeApi + 'add',

    deleteOilType: oilTypeApi + "delete",

    modifyOilType: oilTypeApi + "modify",

    //oil 油品管理

    getOilList: oilApi + 'pagelist',

    addOil: oilApi + 'add',

    deleteOil: oilApi + "delete",

    modifyOil: oilApi + "modify",

    getOil :oilApi + 'oil',

    //菜单管理
    getMenus: menuApi + 'pageList',

    addMenu: menuApi + 'add',

    deleteMenu: menuApi + 'delete',

    modifyMenu: menuApi + 'modify',

    getMenuTree: menuApi + 'menuTree',

    getMenu: menuApi + 'menu',

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



    //省份管理
    getProvinces: provinceApi + 'pageList',

    savaProvince: provinceApi + 'add',

    deleteProvince: provinceApi + 'delete',

}
