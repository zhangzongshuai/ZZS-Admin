<template>
  <div style="padding-bottom: 10px">
    <div 
      v-for="(item, i) in contentList"
      :key="i">
      <div
        :class="{
          card: true,
          cardFlexEnd: item.role == 'user',
          sysDialog: item.role !== 'user',
        }"
        >
        <!-- <img v-if="item.role !== 'user'" src="../../../assets/images/ai.png" alt="" srcset=""> -->
        <div class="miIcon" v-if="item.role == 'user'">U</div>
        <div class="miIcon ai" v-if="item.role !== 'user'">AI</div>
        <div v-if="item.content && item.content !== 'wait'" :class="{ context: true, commonDialog: item.role == 'user' }">
          <!-- <div v-html="mdIt(item.content)"></div> -->
          <div v-if="item.isexplain === 1">
            <div v-for="(answer, j) in item.answers" :key="j">
              <p>{{ answer.text }}</p>
              <p v-if="answer.expand === true" style="color:#2254f4">{{ answer.explain }}</p>
              <el-button v-if="answer.expand === true" type="text" @click="expand(item, j, false)">
                收起
                <i class="el-icon-arrow-up el-icon--right"></i>
              </el-button>
              <el-button v-else type="text" @click="expand(item, j, true)">
                展开
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
            </div>
          </div>
          <div v-if="item.isexplain != 1">
            <p>{{ item.content }}</p>
          </div>
          <!-- <div v-if="item.role !== 'user' && item.answers && item.answers != '' && item.isFold === false" v-html="mdIt(item.answers)">
          </div> -->
        </div>
        
        <div v-if="item.content == 'wait'" :class="{ context: true }">
          <span class="point-flicker"><i class="el-icon-more"></i></span>
        </div>
      </div>
      <div v-if="item.role !== 'user' && item.isDone" class="mr20 mb20">
        <el-tooltip effect="dark" content="复制" placement="top">
          <i class="el-icon-document-copy ml20 toolIcon" @click="copyText(item.content)"></i>
        </el-tooltip>
        <el-tooltip v-if="i === contentList.length - 1" effect="dark" content="重新回答" placement="top">
          <i class="el-icon-refresh ml20 toolIcon" @click="afresh(contentList[contentList.length - 2].content)"></i>
        </el-tooltip>
        <!-- <el-tooltip v-if="item.experience == 1" effect="dark" content="确认" placement="top">
          <i class="el-icon-check ml20 toolIcon" @click="ok(item)"></i>
        </el-tooltip>
        <el-tooltip v-if="item.experience == 1" effect="dark" content="取消" placement="top">
          <i class="el-icon-close ml20 toolIcon" @click="cancel(item)"></i>
        </el-tooltip> -->
        <span v-if="item.experience == 1" style="margin: 0 5px; color: #47a2ff">|</span>
        <el-button v-if="item.experience == 1" type="text" @click="ok(item, i)">
            确认
        </el-button>
        <el-button v-if="item.experience == 1" type="text" @click="cancel(item, i)">
          取消
        </el-button>
      </div>
    </div>

  </div>
</template>

<script>
import MarkdownIt from "markdown-it";
import mdKatex from "@traptitech/markdown-it-katex";
import hljs from "highlight.js";
import MarkdownItCollapsible from "markdown-it-collapsible";

const mdi = new MarkdownIt({
  linkify: true,
  highlight(code, language) {
    const validLang = !!(language && hljs.getLanguage(language));
    if (validLang) {
      return highlightBlock(hljs.highlight(language, code, true).value);
    }
    return highlightBlock(hljs.highlightAuto(code).value);
  },
}).use(MarkdownItCollapsible, {open: false});

function highlightBlock(str) {
  // return `<pre class="code-block-wrapper"><div class="code-block-header"><span class="code-block-header_lang">123123</span><span class="code-block-header_copy">复制</span></div><code class="hljs code-block-body ">${str}</code></pre>`
  return `<pre class="dark code-block-wrapper "><code class="hljs code-block-body ">${str}</code></pre>`;
}

export default {
  props: {
    contentList: {
      type: Array,
    },
  },
  data() {
    return {};
  },
  created() {},
  methods: {
    mdIt(text) {
      return mdi.render(text);
    },
    copyText(text) {
      // 使用 navigator.clipboard.writeText 写入剪贴板
      navigator.clipboard.writeText(text).then(() => {
      this.$message.success('文本已复制到剪贴板');})
      .catch(function(error) {
        // 处理错误
        this.$message.error('无法复制');
      });
    },
    afresh(text) {
      this.$emit('afresh', text)
    },
    expand(item, j, type) {
      item.answers[j].expand = type
    },
    ok(item, i) {
      console.log('ok')
      this.$emit('ok', item, i)
    },
    cancel(item) {
      console.log('cancel')
    }
  },
};
</script>

<style lang="less" scoped>
@contentWidth: 700px;
@themeColor: #fff;
@commonColor: rgba(0, 0, 0, 0);
@themeRadius: 8px;

.sysDialog {
  background: @themeColor;
  color: #41434f;
  border-radius: @themeRadius;
  box-shadow: 0px 5px 10px 0px rgba(57, 59, 60, 0.06) !important;
  cursor: pointer;
  transition: 0.3s;

  &:hover {
    box-shadow: 0px 0px 20px 0px rgba(57, 59, 60, 0.03) !important;
  }
}

.commonDialog {
  background: @commonColor;
  color: #41434f !important;
  // box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.03) !important;
  cursor: pointer;
  transition: 0.3s;

  &:hover {
    // background: rgba(57, 59, 60, 0.2);
  }
}

.cardFlexEnd {
  // justify-content: flex-end;
  // margin-right: 10px;
}

.card {
  position: relative;
  display: flex;
  margin-bottom: 15px;
  transform: 0.3s;
  font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;

  &:hover {
    transition: all 0.3s ease;
    transform: scale(1.005);
  }

  img {
    padding: 7px;
    width: 40px;
    height: 40px;
    border-radius: @themeRadius;
    box-sizing: border-box;
    margin: 5px 10px;
  }

  .context {
    box-sizing: border-box;
    line-height: 25px;
    // min-width: 60px;
    max-width: calc(100% - 70px);
    padding: 15px 0px 15px 0px;
    font-size: 14px;
    // box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.08);
    white-space: pre-wrap;
    
  }
}

/* 设置动画 */
.point-flicker {
  display: inline-block;
  // font-size: 20px;
  height: 100%;
  animation: warn 1.2s ease-out 0s infinite;
}

@keyframes warn {
  0% {
    opacity: 1;
  }

  25% {
    opacity: 0.25;
  }

  50% {
    opacity: 0.5;
  }

  75% {
    opacity: 0.75;
  }

  100% {
    opacity: 1;
  }
}

.miIcon {
  min-width: 30px;
  margin: 12px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background: #414141;
  box-sizing: border-box;
  border-radius: 5px;
  font-size: 13px;
  color: #fff;
}

.ai {
  background-image: linear-gradient(to right, #778dfc, #3d73e9) !important;
}
.toolIcon {
  color: #3d73e9;
  cursor: pointer;
  font-size: 16px;
  font-weight: 800;
}
</style>