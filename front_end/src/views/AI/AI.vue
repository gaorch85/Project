<template>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>大语言模型</span>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input
          v-model="inputString"
          placeholder="请输入你的问题..."
          clearable
          type="textarea"
          :autosize="{ minRows: 2 }">
        </el-input>
      </div>
      <div class="help">
        <el-button type="primary" @click="getPrediction" :loading="loading">获取回答</el-button>
        <el-select v-model="language" placeholder="回答语言" style="margin-left: 20px;">
          <el-option label="任意" value="anything"></el-option>
          <el-option label="中文" value="Chinese"></el-option>
          <el-option label="英文" value="English"></el-option>
        </el-select>
        <div class="times">
          剩余次数：{{ this.times }}
        </div>
      </div>
      <div v-if="prediction" style="margin-top: 20px;">
        <div class="markdown-container">
          <MarkdownContainer :markdownContent="prediction"></MarkdownContainer>
        </div>
      </div>
    </el-card>
  </template>
  
  <script>
    import MarkdownContainer from "../Blog/MarkdownContainer.vue";
  import { api_predict, api_getTimes } from "@/api/ai";

  export default {
    components:{
      MarkdownContainer
    },
    mounted()
    {
        this.getTimes();
    },
    data() {
      return {
        inputString: '',
        prediction: '',
        loading: false,
        language: 'anything',
        times: 0,
      };
    },
    methods: {
      async getPrediction() {
        if(this.times <= 0) {
          this.$message.error('您的次数已用完，请联系管理员！');
          return;
        }
        if (!this.inputString) {
            this.$message.error('请输入你的问题！');
            return;
        }
        this.loading = true;
        var helpPrompt = "";
        helpPrompt = (this.language === "Chinese") ? ",请用中文回答，这非常重要！" : helpPrompt;
        helpPrompt = (this.language === "English") ? ",Please answer in English, but you don't need to repeat this request in your reply, which is more than important for me!" : helpPrompt;
        api_predict({string: this.inputString + helpPrompt})
          .then((response)=>{
            if(response.data.code == 20000)
            {
              this.$message({
                message: '获取成功！',
                type: 'success'
              });
              this.prediction = response.data.data.items;
              this.getTimes();
            }
          })
          .finally(()=>{
            this.loading = false;
          });
      },

      getTimes()
      {
        api_getTimes()
          .then((response)=>
          {
            this.times = response.data.data.items;    
          });
      }
    }
  };
  </script>

<style scoped>
.box-card {
  max-width: 900px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

.help {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-select {
  margin-left: 20px;
}

.times {
  margin-left: auto;
}


</style>
  