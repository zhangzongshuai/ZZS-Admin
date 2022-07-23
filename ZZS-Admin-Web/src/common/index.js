/**
 * 时间格式化处理 时间戳
 * @param time
 * @param style
 * @returns {string}
 */
export const formatDate = (time, style) => {
    var date = new Date(time);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    month = month.toString().length < 2 ? '0' + month : month;

    var day = date.getDate();
    day = day.toString().length < 2 ? '0' + day : day;

    var hour = date.getHours();
    hour = hour.toString().length < 2 ? '0' + hour : hour;

    var minute = date.getMinutes();
    minute = minute.toString().length < 2 ? '0' + minute : minute;

    var second = date.getSeconds();
    second = second.toString().length < 2 ? '0' + second : second;

    var result = '';
    switch (style) {
        case 'YmdHis':
            result = year + '' + month + '' + day + '' + hour + '' + minute + '' + second;
            break;
        case 'Ymd':
            result = year + '' + month + '' + day;
            break;
        case 'Y-m':
            result = year + '-' + month;
            break;
        case 'Y-m-d':
            result = year + '-' + month + '-' + day;
            break;
        case 'Y/m/d':
            result = year + '/' + month + '/' + day;
            break;
        case 'Y-m-d H:i':
            result = year + '-' + month + '-' + day + " " + hour + ':' + minute;
            break;
        case 'Y-m-d H:i:s':
            result = year + '-' + month + '-' + day + " " + hour + ':' + minute + ':' + second;
            break;
        case 'm-d H:i:s':
            result = month + '-' + day + " " + hour + ':' + minute + ':' + second;
            break;
        case 'H:i:s':
            result = hour + ':' + minute + ':' + second;
            break;
        case 'H:i':
            result = hour + ':' + minute;
            break;
        case 'Y':
            result = year;
            break;
        case 'm':
            result = month;
            break;
        case 'd':
            result = day;
            break;
        case 'Y年m月d日':
            result = year + '年' + month + '月' + day + '日';
            break;
        case 'Y年m月':
            result = year + '年' + month + '月';
            break;
        default:
            result = year + '-' + month + '-' + day + " " + hour + ':' + minute + ':' + second;
    }
    return result;
};

export function getBase64(file) {
    return new Promise(function (resolve, reject) {
        // 创建一个reader
        let reader = new FileReader();
        let imgResult = "";

        // 读取file内容
        reader.readAsDataURL(file);

        // 读取成功后的回调
        reader.onload = function () {
            imgResult = reader.result;
        };

        reader.onerror = function (error) {
            reject(error);
        };

        reader.onloadend = function () {
            resolve(imgResult);
        };
    });
}


export function TimeDeel(time) {
    let startTime = time,
        //时间格式化
        byear = startTime.getFullYear(),
        bmonth = startTime.getMonth() + 1,
        bday = startTime.getDate(),
        bhour = startTime.getHours(),
        bmm = startTime.getMinutes(),
        bss = startTime.getSeconds();

    byear = byear.toString().length < 2 ? '0' + byear : byear;
    bmonth = bmonth.toString().length < 2 ? '0' + bmonth : bmonth;
    bday = bday.toString().length < 2 ? '0' + bday : bday;
    bhour = bhour.toString().length < 2 ? '0' + bhour : bhour;
    bmm = bmm.toString().length < 2 ? '0' + bmm : bmm;
    bss = bss.toString().length < 2 ? '0' + bss : bss;

    let startTimes = `${byear}-${bmonth}-${bday} ${bhour}:${bmm}:${bss}`;
    return startTimes;
}