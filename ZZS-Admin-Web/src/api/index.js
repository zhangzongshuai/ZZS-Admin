import {home} from './home.js';
import {systemApi} from "@/api/systemManager"
import {logApi} from "@/api/logManager";


let apiIndex = Object.assign(home, systemApi, logApi)

export default apiIndex
