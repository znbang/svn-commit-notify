<template>
<div>
  <el-input v-model="email" placeholder="請輸入電子郵件" class="email-input">
    <template slot="prepend">電子郵件</template>
    <el-button slot="append" @click="create">新增</el-button>
  </el-input>
  <el-table :data="list" :border="true">
    <el-table-column label="Email" prop="email"></el-table-column>
    <el-table-column label="操作" width="120">
      <template slot-scope="scope">
        <el-button type="danger" size="small" @click="destroy(scope.row)">刪除</el-button>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
const path = '/users'

export default {
  data () {
    return {
      email: '',
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
      this.$axios.post(path, { email: this.email }).then(() => {
        this.email = ''
        this.fetch()
      }).catch(error => {
        this.$message.error(error)
      })
    },
    destroy (user) {
      const params = {
        email: user.email
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
.email-input {
  width: 33%;
  min-width: 20em;
  margin-top: 1em;
  margin-bottom: 1em;
}
</style>
