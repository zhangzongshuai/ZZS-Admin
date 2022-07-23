// 获取组件窗口高度
// value：需要减去的高度
export function showHeight(value) {
    let showheight = 0
    const height = document.body.clientHeight
    if (value) {
        showheight = height - value
        return showheight
    } else {
        return height
    }
}
