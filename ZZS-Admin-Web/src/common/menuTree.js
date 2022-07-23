
export function buildMenuTree(list,id){
    let pidStr = 'parentId';
    let idStr = 'id';
    let childrenStr = 'children';

    let listOjb = {};
    let treeList = [];
    let objList = [];

    for (let i = 0; i < list.length; i++) {
        listOjb[list[i][idStr]] = {
            id : list[i].id,
            label :list[i].name,
        }
        objList.push(listOjb[list[i][idStr]]);
    }
    if (id){
        listOjb[id]['isDisabled'] = true;
    }
    for (let j = 0; j < objList.length; j++) {
        let haveParent = listOjb[list[j][pidStr]]
        if (haveParent) {
            !haveParent[childrenStr] && (haveParent[childrenStr] = [])
            haveParent[childrenStr].push(objList[j])

        } else {
            treeList.push(objList[j])
        }
    }
    return treeList
}