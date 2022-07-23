
let api = "/api/accesslog/"
let loginLogApi = "/api/loginlog/"



export const logApi = {
    getAccessLog : api + 'pagelist',

    addAccessLog : api + 'add',

    getLoginLog : loginLogApi + 'pagelist',
}

