<template>
<div>
  <el-input v-model="url" placeholder="請輸入URL" class="url-input">
    <template slot="prepend">URL</template>
    <el-button slot="append" @click="create">新增</el-button>
  </el-input>
  <el-table :data="list" :border="true">
    <el-table-column label="URL" prop="url"></el-table-column>
    <el-table-column label="Revision" prop="revision" width="120"></el-table-column>
    <el-table-column label="操作" width="120">
      <template slot-scope="scope">
        <el-button type="danger" size="small" @click="destroy(scope.row)">刪除</el-button>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
const path = '/repositories'

export default {
  data () {
    return {
      url: '',
      list: []
    }
  },
  asyncData (ctx) {
    const axios = ctx.app.$axios
    return axios.get(path).then(response => {
      return { list: response.data }
    }).catch(error => {
      ctx.app.$message.error(error)
    })
  },
  methods: {
    fetch () {
      this.$axios.get(path).then(response => {
        this.list = response.data
      }).catch(error => {
        this.$message.error(error)
      })
    },
    create () {
      this.$axios.post(path, { url: this.url }).then(() => {
        this.url = ''
        this.fetch()
      }).catch(error => {
        this.$message.error(error)
      })
    },
    destroy (monitor) {
      const params = {
        url: monitor.url
      }
      this.$axios.delete(path, {params: params}).then(() => {
        this.fetch()
      }).catch(error => {
        this.$message.error(error)
      })
    }
  }
}
</script>

<style scoped>
.url-input {
  width: 33%;
  min-width: 30em;
  margin-top: 1em;
  margin-bottom: 1em;
}
</style>
