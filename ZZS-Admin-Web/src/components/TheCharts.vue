<template>
  <div class="echartsLine"
       :id="id"
       :style="style">
  </div>
</template>

<script>
export default {
  name: "TheCharts",
  props: {
    id: {
      type: String
    },
    width: {
      type: String,
      default: "100%"
    },
    height: {
      type: String
    },
    option: {
      type: Object
    }
  },
  data() {
    return {
      MyEcharts: "" //echarts实例
    };
  },
  computed: {
    style() {
      return {
        height: this.height,
        width: this.width
      };
    }
  },
  watch: {
    //要监听的对象 option
    //由于echarts采用的数据驱动，所以需要监听数据的变化来重绘图表
    option: {
      handler(newVal, oldVal) {
        if (this.MyEcharts) {
          if (newVal) {
            this.MyEcharts.setOption(newVal, true);
          } else {
            this.MyEcharts.setOption(oldVal, true);
          }
        } else {
          this.InitCharts();
        }
      },
      deep: true //对象内部属性的监听
    }
  },
  mounted() {
    this.InitCharts();
  },
  methods: {
    InitCharts() {
      this.MyEcharts = this.$echarts.init(document.getElementById(this.id));
      this.MyEcharts.clear(); //适用于大数据量的切换时图表绘制错误,先清空在重绘
      this.MyEcharts.setOption(this.option, true); //设置为true可以是图表切换数据时重新渲染
      let _this = this;
      window.addEventListener("resize", function() {
        _this.MyEcharts.resize();
      });
    }
  }
}
</script>

<style scoped>

</style>