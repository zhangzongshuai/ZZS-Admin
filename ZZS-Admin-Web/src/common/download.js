
import axios from 'axios';


export const download = (url,fileName,data,params)=>{
    let config = {
        responseType :"blob"
    };
    if (params){
        config.params = params
    }
    axios.post(url,data,config).then(function (res){
        let fileUrl = window.URL.createObjectURL(new Blob([res]));
        // 生成一个a标签
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = fileUrl;
        if (!fileName){
            fileName = new Date().getTime();
        }
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
    }).catch(function (err){
        console.error("download:",err)
    });
}