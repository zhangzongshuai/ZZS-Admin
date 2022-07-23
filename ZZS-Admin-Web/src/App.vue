<template>
  <div id="app" class="wh">
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'App'
}
</script>

<style lang="scss">
//@import './assets/styles/main';
@import "./assets/style/base.css";
@import "./assets/style/common.css";
@import "./assets/style/style.css";
#nprogress .bar {
  background: #890340 !important; //自定义颜色
}
</style>
