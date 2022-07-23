/*
    系统管理模块
 */

let oilApi = '/api/oil/'
let userApi = '/api/user/'
let oilTypeApi = '/api/oiltype/'
let stationApi = '/api/station/'
let menuApi = '/api/menu/'
let roleApi = '/api/role/'
let provinceApi = '/api/province/'

export const systemApi = {

    //user用户管理
    getUsers: userApi + 'pagelist',

    modifyPassworld: userApi + 'modifyPassword',

    resetPwd: userApi + 'resetpwd',

    deleteUser: userApi + 'deleteuser',

    addUser: userApi + 'addUser',

    getUser: userApi + 'user',

    modifyUser: userApi + 'modifyuser',

    configUserRole : userApi + 'configuserrole',

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
    getMenus: menuApi + 'pagelist',

    addMenu: menuApi + 'add',

    deleteMenu: menuApi + 'delete',

    modifyMenu: menuApi + 'modify',

    getMenuTree: menuApi + 'menutree',

    getMenu: menuApi + 'menu',

    //角色管理

    getRoles: roleApi + "pagelist",

    getRole: roleApi + "role",

    deleteRole: roleApi + 'delete',

    modifyRole: roleApi + 'modify',

    addRole: roleApi + 'add',

    getRoleMenus: roleApi + 'rolemenus',

    configRoleMenu: roleApi + 'configrolemenu',

    getRoleUsers: roleApi + 'usersByRoleId',

    configRoleUser: roleApi + 'configroleuser',

    getUserRoles : roleApi + 'rolesByUserId',

    //省份管理
    getProvinces: provinceApi + 'pageList',

    savaProvince: provinceApi + 'add',

    deleteProvince: provinceApi + 'delete',

}
