

let api = '/api/ymdz/'

export const ymdzApi = {
    getDfyfList :  api + "dfyflist",

    getJFPageList : api + "dfyfpagelist",

    // 删除贷方
    deleteDf: api+ "deleteCreditor",

    // 删除借方
    deleteJf: api + "deleteDebtor",

    getYsyfList :  api + "ysyflist",

    getDFPageList : api + "ysyfpagelist",

    checkYF : api + "checkyf",

    autoCheck : api + "checkyf",

    manualCheck:api + "manualcheck",

    downloadJfExcel : api + "downloadjf",

    downloadDfExcel : api + "downloaddf",

    getDzHistory : api + "checkhistory",

    //撤销对账
    revocationDZ : api + "revocation",
}
