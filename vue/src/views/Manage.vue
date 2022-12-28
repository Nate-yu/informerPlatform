<template>
    <el-container style="min-height: 100vh">

        <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
            <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
        </el-aside>

        <el-container>
            <el-header style="border-bottom: 1px solid #ccc;">
                <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :person="person"/>
            </el-header>

            <el-main>
                <!--表示当前页面的子路由会在 <router-view /> 里面展示-->
                <router-view @refreshUser="getUser"/>
            </el-main>

        </el-container>
    </el-container>
</template>

<script>

    import Aside from "../components/Aside";
    import Header from "../components/Header";
    import request from "../utils/request";

    export default {
        name: 'Manage',
        data() {
            return {
                person:{},
                collapseBtnClass: 'el-icon-s-fold',
                isCollapse: false,
                sideWidth: 200,
                logoTextShow: true,
            }
        },
        components: {
            Aside,
            Header
        },
        created() {
            // 从后台获取最新的User数据
            this.getUser()
        },
        methods: {
            collapse() {  // 点击收缩按钮触发
                this.isCollapse = !this.isCollapse;
                if (this.isCollapse) {  // 收缩
                    this.sideWidth = 64;
                    this.collapseBtnClass = 'el-icon-s-unfold';
                    this.logoTextShow = false
                } else {   // 展开
                    this.sideWidth = 200;
                    this.collapseBtnClass = 'el-icon-s-fold';
                    this.logoTextShow = true
                }
            },
            // 获取用户的最新数据
            getUser() {
                let name = localStorage.getItem("person") ? JSON.parse(localStorage.getItem("person")).name : ""
                if (name) {
                    // 从后台获取User数据
                    request.get("/user/name/" + name).then(res => {
                        // 重新赋值后台的最新User数据
                        this.person = res.data
                    })
                }
            }
        }
    }
</script>