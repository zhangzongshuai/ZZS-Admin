
/*
* 结算管理
* */

let incrementApi = '/api/increment/'
let gaugeApi = '/api/gaugeticket/'
let settlementApi = '/api/settlement/'
let priceApi = '/api/oilPrice/'
let configPlanApi = '/api/configplandetail/'
let configStationApi = '/api/configPlanStation/'
let progressApi = "/api/planProgress/"
let cqlApi = "/api/cql/"
let clcjApi = "/api/clcj/"


export const settlementManagerApi = {

    //增量计划
    addIncrement: incrementApi + "add",

    getIncrements:incrementApi + "pagelist",

    deleteIncrement : incrementApi + "delete",

    getIncrement : incrementApi + "increment",

    getIncrementBalance : incrementApi + "incrementBalance",

    modifyIncrement : incrementApi + 'modify',

    changeIncrementPrice: incrementApi + 'changeIncrementPrice',

    //计量单管理
    syncTrain: gaugeApi + 'synctrain',

    syncPipeline:gaugeApi + 'syncpipeline',

    getTrainGauge:gaugeApi + 'traingaugepage',

    getJSJLD : gaugeApi + 'jsjld',

    splitJSJLD : gaugeApi + 'split',

    //getPipelineGauge:gaugeApi + 'pipelinegaugepage',

    allJsJld : gaugeApi + "allJsJld",

    //结算
    settlement:settlementApi + 'settlement',

    settlementList : settlementApi + 'settlementlist',

    getSettmentFinished:settlementApi + 'finished',

    settlementPreview:settlementApi + 'settlementPreview',

    cancleSettlement : settlementApi + 'cancle',

    clcjResidue : settlementApi + 'clcjResidue',

    getPlanAndFinished: settlementApi + 'planAndFinished',

    clearPipe: settlementApi + "clearPipe",

    getPipeNotClear : settlementApi + 'getPipeNotClear',

    //价格管理
    getPriceList :priceApi + 'page',

    changeOilPrice : priceApi + 'changeV2',

    modifyPrice:priceApi + 'modify',

    //配置计划

    addConfigPlan : configPlanApi + 'add',

    getConfigPlanList : configPlanApi + 'configPlanList',

    changeConfigPlan : configPlanApi + 'change',

    modifyPlan : configPlanApi + 'modify',

    lastPlan : configPlanApi + 'lastplan',

    modifyChange: configPlanApi + 'modifychange',

    currentPlan : configPlanApi + 'currentPlan',

    addStationPlan: configStationApi+ 'addStationPlan',

    changeStationPlan: configStationApi + 'changeStationPlan',

    getConfigStationPlan: configStationApi + 'getConfigPlanStation',


    //从量从价信息

    getCjcl: clcjApi + 'page',

    changeClcjPrice : clcjApi + 'changeClcjPrice',

    //从量从价计划对账表
    clcjdz: settlementApi + 'clcjdz',

    clcjPrice : settlementApi + 'clcjPrice',

    //记录价格
    recordPrice : settlementApi + 'recordprice',

    //是否在从量从价
    isclcj:settlementApi + "isclcj",

    //预结算
    yujie : settlementApi + "yujie",

    //保存冲销预结算的计量单
    saveyujiejld : settlementApi + 'saveyujiejldV2',

    //计划进度
    planProgress : progressApi + 'planProgress',

    //结算进度
    settlementProgress : progressApi + 'settlementProgress',

    //超欠量
    getCql : cqlApi + 'page',

    confirmCql: cqlApi + 'confirm',

}
