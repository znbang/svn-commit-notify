<template>
  <div>
    <el-menu mode="horizontal" :router="true" :default-active="activeIndex">
      <el-menu-item index="1" route="/">Repository</el-menu-item>
      <el-menu-item index="2" route="/user">User</el-menu-item>
    </el-menu>
    <nuxt/>
  </div>
</template>

<script>
export default {
  data () {
    return {
      activeIndex: '1'
    }
  }
}
</script>
