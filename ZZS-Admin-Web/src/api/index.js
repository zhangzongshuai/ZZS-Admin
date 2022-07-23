import {home} from './home.js';
import {ymdzApi} from './ymdz'
import {systemApi} from "@/api/systemManager"
import {ddyfApi} from "@/api/ddyf";
import {settlementManagerApi} from "@/api/settlementManager";
import {logApi} from "@/api/logManager";
import {productApi} from "@/api/productManager"


let apiIndex = Object.assign(home, ymdzApi, systemApi, ddyfApi, settlementManagerApi, logApi, productApi)

export default apiIndex