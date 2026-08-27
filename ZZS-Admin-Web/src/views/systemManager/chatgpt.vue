<template>
  <div class="container">
    <div class="popup" ref="popup" @click="popupClose">
      <div class="con_left" @click.stop>
        <button class="new_talk" @click="newTalk">开始新对话{{ type }}</button>

        <div class="talk_list">
          <div v-for="(item, index) in talkList" :key="index" @click="reTalk(item)" :class="{ 'border': tid == item.id }">
            <span>{{ item.data[0].content }}</span>
            <i class="el-icon-delete deleteTalk" @click.stop="deleteTalk(item.id)"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="con_right" ref="con_right">
      <div class="header" ref="header">
        <div class="more_button" @click="popupShow"><i class="el-icon-s-operation"></i></div>
        <!-- <img src="../../assets/images/aslogo.png" alt="" srcset=""> -->
        <span>AI Scene</span>
      </div>
      <div class="content">
        <div class="limit" v-if="messages.length <= 0">
          <!-- <div class="primary_con">
            <div class="">
              <i class="el-icon-connection"></i>
              <h3>AI能力</h3>
              <p @click="assign('scene是什么意思')">scene是什么意思</p>
              <p @click="assign('莲藕排骨汤怎么做')">莲藕排骨汤怎么做</p>
              <p @click="assign('今天的世界有哪些新闻热点')">今天的新闻热点</p>
              <p @click="assign('查询xx星座运势')">查询星座运势</p>
              <p @click="assign('有趣的科学实验')">有趣的科学实验</p>
              <p @click="assign('全球高分电影推荐')">全球高分电影推荐</p>
              <p>
                <i class="el-icon-more"></i>
              </p>
            </div>
            <div class="">
              <i class="el-icon-chat-line-square"></i>

              <h3>AI办公</h3>
              <p @click="assign('文章润色')">文章润色</p>
              <p @click="assign('写一篇方案报告')">写一篇方案报告</p>
              <p @click="assign('生成一篇关于xx的日报/周报')">生成一篇日报/周报</p>
              <p @click="assign('撰写一篇邮件/演讲稿')">撰写一篇邮件/演讲稿</p>
              <p @click="assign('代码报错解决')">代码报错解决</p>
              <p>
                <i class="el-icon-more"></i>
              </p>
            </div>
          </div> -->
          <div class="tip">我是工控之友，您可以向我进行提问</div>
        </div>

        <div v-show="messages.length > 0" class="content_list" ref="scrollDiv">
          <div class="talk_con">
            <contentList :contentList="messages" @afresh="assign" @ok="ok"></contentList>
          </div>
        </div>
      </div>
      <div class="footer">
        <div class="input_con">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5}" :placeholder="disabled ? '获取中...' : '发送消息给AI'" v-model="content"
            @keydown.native="send($event)">
          </el-input>
          <div class="sub_btn" @click="send()">
            <i v-if="!disabled" class="el-icon-s-promotion"></i>
            <i v-else class="el-icon-loading"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { openChat } from "@/api/api";
import md5 from 'js-md5'
import contentList from "@/components/content-list"

export default {
  name: "AI",
  components: {
    contentList
  },
  data() {
    return {
      // 内容
      content: "",
      messages: [],
      disabled: false,
      dialogVisible: false,
      //  打赏弹窗
      reward: false,
      //
      wxCode: false,
      talkList: [],
      tid: localStorage.getItem("talkId"),
      type: this.$route.query.type,
      conversation_id: ''
    };
  },
  created() {
    this.messages = []
    // 消息记录
    // this.messages = localStorage.getItem("messages") ? JSON.parse(localStorage.getItem("messages")) : []
    // 会话记录
    // this.talkList = localStorage.getItem("talkList") ? JSON.parse(localStorage.getItem("talkList")) : []
    this.get_talkList()
    setTimeout(_ => {
      this.handleScrollBottom() //滚动至最底部
    }, 100)

  },
  mounted() {

  },
  methods: {

    get_talkList(){
      this.talkList = localStorage.getItem("talkList") ? JSON.parse(localStorage.getItem("talkList")) : []
    },

    async send_test(e){
      if (this.disabled){
        return false
      }
      console.log(e)
      if (e){
        // Enter
        if (e.keyCode == 13){
          e.preventDefault()
        }
        else{
           return false
        } 
      }
      this.handleScrollBottom()
      if (!this.content) {
        return
      }
      this.disabled = true
      let timeStamp = this.getTimeStamp()
      let user = {
        "role": "user",
        "content": this.content.trim()
      }
      let system = {
        "role": "assistant",
        "content": "wait",
        "isDone": false,
        "isFold": true,
        "answers": "",
        "experience": 0
      }
      this.messages.push(user)
      this.messages.push(system)

      let format = this.content.trim() + timeStamp
      let sign = md5(format)

      this.content = ""
      this.handleScrollBottom() //滚动至最底部

      // 模拟请求AI获取流式数据
      let res = '当前AI技术发展前景广阔，正处于快速发展的黄金时期。随着算法优化、大数据积累和计算能力提升，AI技术将在多个领域实现重大突破。首先，在智能制造领域，AI将推动工业4.0的实现，提高生产效率和产品质量。其次，在医疗健康领域，AI辅助诊断、个性化治疗和药物研发将带来医疗变革。此外，自动驾驶、智慧城市、金融科技等领域也将因AI技术的融入而焕发新生。然而，AI技术的发展也面临伦理、隐私保护等挑战。总体而言，未来AI技术将在推动产业升级、改善民生、助力社会治理等方面发挥重要作用，为人类社会带来深远影响。\n\n以下是markdowm语法高亮演示\n\n#### 使用Python内置的`sorted()`函数或列表的`sort()`方法可以快速对列表进行排序：\n```python\n# 使用sorted()函数\nsorted_list = sorted([3, 1, 4, 1, 5, 9])\n# 使用sort()方法\nmy_list = [3, 1, 4, 1, 5, 9]\nmy_list.sort()\n```'
      let stop = 10
      let f = setInterval(() => {
        if (stop > res.length){
          stop = res.length
        }
        let aaa = res.substring(0,stop)
        this.messages[(this.messages.length - 1)].content = aaa
        this.handleScrollBottom() //滚动至最底部
        if (stop >= res.length){
          this.messages[(this.messages.length - 1)].answers = "这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;这是一个测试语句;"
          this.messages[(this.messages.length - 1)].experience = 1
          this.messages[(this.messages.length - 1)].isDone = true
          clearInterval(f)
        }
        else{
          stop += 10
        }
        
      }, 100)
      this.handleScrollBottom() //滚动至最底部
      this.disabled = false
    },

    async send(e) {
      if (this.disabled){
        return false
      }
      console.log(e)
      if (e){
        // Enter
        if (e.keyCode == 13){
          e.preventDefault()
        }
        else{
           return false
        } 
      }
      this.handleScrollBottom()
      if (!this.content) {
        return
      }

      this.disabled = true
      let timeStamp = this.getTimeStamp()
      let user = {
        "id":"",
        "role": "user",
        "content": this.content.trim()
      }
      let system = {
        "id": "",
        "role": "assistant",
        "content": "wait",
        "isDone": false,
        "answers": [],
        "isexplain": 0,
        "experience": 0
      }

      let newList = []
      for (let i = this.messages.length - 1; i >= 0; i--){
        if (this.messages[i]['content'] == "对不起，好像出现了点问题，请重试一下吧..." || this.messages[i]['content'] == "wait"){
          // 如果回答有问题,提问也应该跳过
          i = i -1
          continue
        }
        newList.unshift({
            position: this.messages[i]['role'], 
            info: this.messages[i]['content']
        })
        if(newList.length == 20){
          break
        }
      }
      newList.push({
        position: user.role, 
        info: user.content
      })

      this.messages.push(user)
      this.messages.push(system)


      this.content = ""
      this.handleScrollBottom() //滚动至最底部

      //------------------------------
      try {
        // 用户输入的内容
        let user_content = user.content
        // let user_content = '热介质膨胀罐的作用'

        const history = encodeURIComponent(JSON.stringify(newList));
        
        let controller = new AbortController();
        let url = `/haiyou/stream?req_question=${user_content}&temperature=0.01&top_p=0.1&max_length=8192&req_history=${history}&experience=0`
        await fetch(url, 
        {
          method: "GET",
          signal: controller.signal,
        }).then( async (response) => {
          if (!response.ok) {
            // this.messages[(this.messages.length - 1)].content = "对不起，好像出现了点问题，请重试一下吧..."
            throw new Error('Network response was not ok');
          }
          const reader = response.body.getReader();
          const textDecoder = new TextDecoder();
          let result = true;
          let allAiData = ""
          while (result) {
            const { done, value } = await reader.read();
            if (done) {
              // 数据传输完毕,此处可以把本次请求的存入数据库作为会话历史记录
              console.log('Stream ended');
              result = false;
              this.disabled = false
              this.messages[(this.messages.length - 1)].isDone = true
              this.handleScrollBottom()
              break;
            }
            
            // 这里这么处理是因为获取数据流时，每次获取到的可能不是完整的一条event:message
            // 会出现截断的情况，所以把所有的event:message拼接在一起进行分割
            const chunkText = textDecoder.decode(value);
            allAiData += chunkText
            let temp = allAiData.split("data:")
            temp = temp.filter(item => item !== "");

            let output = ''
            for (let index = 0; index < temp.length; index++) {
              const element = temp[index];
              try{
                if (element.indexOf(': ping') >= 0){
                  element = element.split(': ping')[0]
                }
                let aiData = JSON.parse(element)
                let current_message = this.messages[(this.messages.length - 1)]
                current_message.isexplain = aiData.isexplain
                current_message.experience = aiData.experience
                if (aiData.isexplain === 1) {
                  output = aiData.text
                  let temp = aiData.answers
                  for (let j = 0; j < temp.length; j++) {
                    temp[j].expand = false
                  }
                  current_message.answers =  temp
                }
                else{
                  output += aiData.text
                }
                current_message.content = output
                this.handleScrollBottom() //滚动至最底部
              }
              catch (e) {
                console.error(e)
                console.log(element)
                break
              }
            }
            
          }
        })
      } catch (e) {
        console.error(e);
        this.disabled = false
        this.messages[(this.messages.length - 1)].content = "对不起，好像出现了点问题，请重试一下吧..."
        this.handleScrollBottom() //滚动至最底部
      }
      // ------------------------------------------
    },
    getTimeStamp() {
      let dateNow = new Date()
      let nowTime = dateNow.getTime()
      return nowTime
    },
    handleScrollBottom() {
      this.$nextTick(() => {
        let scrollElem = this.$refs.scrollDiv;
        scrollElem.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' });
      });
      localStorage.setItem("messages", JSON.stringify(this.messages))
    },
    handleClose(done) {
      done();
    },
    popupShow() {
      this.$refs.popup.style = 'left:0;background: rgba(0, 0, 0, 0.5)'
    },
    popupClose() {
      this.$refs.popup.style = 'left:-100vw'
    },
    assign(text) {
      this.content = text
      this.send()
    },
    // 删除会话
    deleteTalk(tid) {
      this.talkList = this.talkList.filter(item => item.id != tid)
      this.talkList = this.talkList.slice(0, 8)
      localStorage.setItem("talkList", JSON.stringify(this.talkList))
      this.$messages.success("删除成功")
    },
    // 保存历史数据据
    saveHistory() {
      let obj = {
        id: 'scene' + this.getTimeStamp(),
        data: this.messages
      }
      if (this.messages.length > 0) {
        if (localStorage.getItem("talkId")) {
          this.talkList.map(item => {
            if (item.id == localStorage.getItem("talkId")) {
              item.data = this.messages
            }
          })
        } else {
          this.talkList.unshift(obj)
        }
        localStorage.removeItem("talkId")
        this.tid = ''
        this.talkList = this.talkList.slice(0, 8)
        localStorage.setItem("talkList", JSON.stringify(this.talkList))
      }
    },
    newTalk() {
      this.saveHistory()

      this.messages = []
      localStorage.setItem("messages", JSON.stringify([]))
      this.popupClose()
    },
    reTalk(item) {
      this.saveHistory()

      localStorage.setItem("talkId", item.id)
      this.tid = item.id
      this.messages = item.data
      this.popupClose()
      setTimeout(_ => {
        this.handleScrollBottom() //滚动至最底部
      }, 100)
    },
    ok(item, i){
      item.isOk = true
      this.messages[i].isOk = true
    }
  }
};
</script>

<style lang="less" scoped>
@contentWidth: 800px;
@themeColor: #4684ff;
// @themeColor: #4684ff;
@commonColor: #eee;
@themeRadius: 6px;



.container {
  display: flex;
  justify-content: space-between;
  height: 82vh;
  width: 82vw;
  overflow: hidden;
  min-width: 100px;
  transition: 0.6s;
  background-image: url("https://luvi.gitee.io/ai/publicImages/login_background.c41c40af.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  backdrop-filter: blur(10px);
}

.con_left {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  // justify-content: center;
  min-width: 250px;
  max-width: 250px;
  background: rgba(255, 255, 255, 1);
  height: 100%;
  padding: 20px 0;
  box-sizing: border-box;
  transition: 0.5s;

  .talk_list {
    width: 80%;
    margin-top: 10px;
    color: #4c4c4c;

    .border {
      border: 1px solid #778dfc;
      background: #f2f6ff;

      color: #6e86ff;

      &:hover {
        background: #f0f4ff;
      }
      font-weight: bold;
    }
    .deleteTalk {
        color: rgb(140, 50, 50);
        cursor: pointer;
      }
  
    div {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 45px;
      border: 1px solid #eee;
      border-radius: 6px;
      padding: 0 15px 0 15px;
      font-size: 14px;
      box-sizing: border-box;
      cursor: pointer;
      margin-top: 10px;
      color: #595959;

      span {
        display: inline-block;
        text-align: left;
        width: 80%;
        overflow: hidden;
        word-break: break-all;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
      }

      &:hover {
        background-image: linear-gradient(to right, #eee, #eee);
      }
    }
  }

  .copyright {
    position: absolute;
    bottom: 15px;
    width: 90%;
    border: 1px solid #eee;
    display: flex;
    align-items: center;
    padding: 10px;
    box-sizing: border-box;
    border-radius: 8px;

    img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-right: 10px;
    }

    div {
      color: #424242;

      h2 {
        font-size: 15px;
      }
    }

    p {
      font-size: 13px;
      margin-top: 2px;
    }

    a {
      color: #266dfb;
      font-weight: normal !important;
    }
  }

  .new_talk {
    border: none;
    width: 80%;
    height: 45px;
    font-weight: bold;
    letter-spacing: 1px;
    margin-bottom: 16px;
    background-image: linear-gradient(to right, #778dfc, #3D73E9);

    border-radius: @themeRadius;
    box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.08);
    color: #fff;
    cursor: pointer;
    transition: 0.2s;

    &:hover {
      background-image: linear-gradient(to right, #6b83fb, #3d8de4);
    }

    &:active {
      background-image: linear-gradient(to right, #6b83fb, #3d8de4);
    }
  }

  .other {
    position: absolute;
    bottom: 90px;
    width: 90%;
    border-radius: 6px;
    overflow: hidden;

    .bar {
      line-height: 45px;
      // border-bottom: 1px solid #ddd;
      font-weight: bold;
      // background: #eeeeee;
      color: #5a5a5a;
      box-sizing: border-box;
      cursor: pointer;
      font-size: 14px;
      padding: 0 15px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      &:hover {
        background: #eee;
      }

      &:nth-child(3) {
        border: none;
      }
    }
  }
}

.con_right {
  position: relative;
  min-width: 800px;
  width: 100%;
  height: 100%;
  // background: #ECEFF6;
  // background-image: linear-gradient(45deg, #f6f8ff, #E2E8FF);


  .header {
    position: absolute;
    z-index: 1;
    width: 100%;
    display: flex;
    display: none;
    align-items: center;
    justify-content: center;
    background-image: linear-gradient(to left, #5185ff, 1px, #5185ff);
    backdrop-filter: blur(8px);
    height: 45px;

    span {
      font-size: 16px;
      text-align: center;
      color: #ffffff;
      font-weight: bold;
    }

    img {
      width: 140px;
    }

    .more_button {
      display: none;
      position: absolute;
      left: 15px;
      font-size: 23px;
      width: 35px;
      height: 35px;
      color: #fff;
      text-align: center;
      line-height: 35px;

      &:active {
        background: rgba(0, 0, 0, 0.1);
        border-radius: 3px;
      }
    }
  }

  .content {
    width: 100%;

    .limit {
      margin: auto;
      width: @contentWidth;
      margin-top: 60px;
    }

    .tip {
      padding-left: 6px;
      border-left: 3px solid #266dfb;
      margin-left: 20px;
      box-sizing: border-box;
      font-size: 18px;
      color: #4a4a4a;
    }

    .primary_con {
      padding: 20px 10px;
      box-sizing: border-box;
      display: flex;
      justify-content: center;
      text-align: center;

      h3 {
        margin-bottom: 15px;
        color: #303030;
      }

      div {
        width: 50%;
        padding: 10px;
        box-sizing: border-box;
      }

      p {
        line-height: 35px;
        margin-top: 10px;
        background: #f7f7f8;
        border-radius: 5px;
        font-size: 13px;
        color: #4a4a4a;
        cursor: pointer;

        &:hover {
          background: #e9e9ea;
        }

        &:active {
          background: #d6d6d6;
        }

        i {
          font-size: 15px;
        }
      }

      i {
        font-size: 26px;
      }
    }

    .content_list {
      width: calc(100%);
      padding-top: 50px;
      padding-bottom: 50px;
      // padding-left: 3px;
      box-sizing: border-box;
      overflow: scroll;
      height: calc(75vh);
      overflow-x: hidden;
    }

    .talk_con {
      margin: auto;
      width: calc(@contentWidth);
    }
    /* 1,滚动条 */
  ::-webkit-scrollbar {
        width: 0px !important;
        /* 纵向滚动条 宽度 */
        height: 0px !important;
        /* 横向滚动条 高度 */
    }

  /* 5,滑块 */
  ::-webkit-scrollbar-thumb {
      background: #b1b1b1 !important;
      min-height: 50px !important;
      max-height: 100vh !important;
  }
  }


  // footer总高度175px
  .footer {
    position: absolute;
    // background: #ECEFF6;
    // padding-top: 30px;
    // background-image: linear-gradient(to bottom, rgba(0, 0, 0, 0), 3%, #ECEFF6);
    backdrop-filter: blur(20px);

    bottom: 0px;
    width: 100%;

    .input_con {
      position: relative;
      margin: auto;
      width: @contentWidth + 100px;

      .sub_btn {
        position: absolute;
        right: 10px;
        line-height: 38px;
        border-radius: 5px;
        bottom: 10px;
        width: 38px;
        text-align: center;
        color: #fff;
        cursor: pointer;
        transition: 0.2s;
        font-size: 21px;
        background-image: linear-gradient(to right, #778dfc, #3D73E9);


        &:hover {
          background-image: linear-gradient(to right, #778dfc, #6178ec);
        }
      }

      // .el-input {
      //   position: absolute;
      //   top: 0;
      //   left: 0;
      //   box-sizing: border-box;
      //   border: 1px solid #e9e9e9;
      //   width: 100%;
      //   line-height: 45px;
      //   padding: 0 45px 0 15px;
      //   border-radius: 6px;
      //   // height: 50px;
      //   transition: 0.3s;
      //   letter-spacing: 0.5px;
      //   color: #383838;
      //   box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.02);

      //   &:focus {
      //     outline: none;
      //     border: 1px solid @themeColor;
      //     box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.03);
      //   }
      // }

      /deep/ .el-textarea__inner {
        box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.02);
        border-radius: 8px;
        font-family: 'black';
        padding: 10px 10px;
        box-sizing: border-box;

        &:focus {
          outline: none;
          border: 1px solid @themeColor;
          box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.03);
        }
      }



    }

    p {
      margin-top: 15px;
      margin-bottom: 20px;
      box-sizing: border-box;
      font-size: 10px;
      color: #909090;
      text-align: center;

      span {
        text-decoration: underline;
        color: @themeColor;
        cursor: pointer;
      }
    }
  }

}

/* android适配css 从下开始 */
@media (max-width: 800px) {

  .popup {
    position: absolute;
    left: -100vw;
    z-index: 4;
    height: 100%;
    width: 100vw;
    transition: 0.5s;

  }

  .con_left {
    max-width: 200px;

  }

  .con_right {
    min-width: 100%;

    .limit {
      max-width: 100%;
    }

    .header {
      display: block;
      display: flex;
      background-image: linear-gradient(to right, #778dfc, #3D73E9);

    }

    .more_button {
      display: block !important;
      color: #fff !important;
    }

    .talk_con {
      max-width: 100%;
    }
  }

  .input_con {
    max-width: 95%;
  }

  .content_list {
    max-width: calc(100% - 30px);
    margin: auto;
  }

  /deep/ .el-dialog {
    width: 85% !important;
  }
}
</style>
