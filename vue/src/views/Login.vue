<template>
    <div class="wrapper">
        <div style="margin: 200px auto;width: 350px; height: 300px; padding: 20px; border-radius: 10px" class="login_form">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>信息员平台</b></div>
            <el-form :model="person" :rules="rules" ref="personForm">
                <el-form-item prop="account">
                    <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user"
                              v-model="person.account"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                              v-model="person.password"></el-input>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: right">
                    <el-button type="primary" size="small" autocomplete="off" @click="login">登 录</el-button>
                </el-form-item>
            </el-form>
        </div>
        <footer style="text-align: center;font-size: 15px;color: black">
            Copyright &copy; 2022 开发者 - <a href="https://gitee.com/nate-yu" title="Nate's Gitee">余 彬</a> | <a href="About.vue">关 于</a>
        </footer>
    </div>

</template>

<script>
    import request from "../utils/request";

    export default {
        name: "Login",
        data() {
            return {
                person: {
                    account: '',
                    password: ''
                },
                rules: {
                    account: [
                        {required: true, message: '请输入学号或用户名', trigger: 'blur'},
                        {min: 1, max: 11, message: '长度在 1 到 11 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                }
            }
        },
        created () {
            // “登录”按钮绑定回车键
            let that = this;
            document.onkeydown = function (e) {
                e = window.event || e;
                // 验证在登录界面和按的键是回车键enter
                if (that.$route.path === '/login' && (e.code === 'Enter' || e.code === 'enter')) {
                    // 登录事件
                    that.login();
                }
            }
        },
        methods: {
            login() {
                this.$refs['personForm'].validate((valid) => {
                    if (valid) {  // 表单校验合法
                        request.post("/login/login", this.person).then(res => {
                            if(res.data) {
                                localStorage.setItem("person", JSON.stringify(res.data));  // 存储用户信息到浏览器
                                this.$router.push("/home");
                                this.$message.success("登录成功")
                            } else {
                                this.$message.error(res.msg)
                            }
                        })
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .wrapper {
        height: 100vh;
        background-image: url("../assets/background.png");
        -moz-background-size:100% 100%;
        background-size:100% 100%;
        overflow: hidden;
    }
    .bg-purple {
        background: #d3dce6;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 36px;
    }

    .login_form {
         width: 100%;
         border: 1px solid transparent;
         border-radius: 15px;
         padding-right: 30px;
         /* 为其整体设置接近透明的效果*/
         background-color: rgba(255, 255, 255, .5);
         /* 设置box-shadow使其有立体感 */
         box-shadow: 3px 3px 0 0 rgba(0, 0, 0, 0.2);
         position: relative;
         left: 6px;
     }
    a {
        text-decoration: none;
        color: black;
        font-size: 15px;
    }
    footer {
        bottom: 10px;
        position: fixed;
        left: 50%;
        transform: translateX(-50%);
        width: fit-content;
        font-size: 12px;
        color: rgba(255,255,255,.6);
        text-align: center;
        text-shadow: 0 0 20px rgb(0 0 0 / 80%);
        white-space: nowrap;
        animation-name: fadeIn;
        animation-duration: 1s;
        animation-iteration-count: 1;
        animation-delay: 0s;
    }
    .el-input__inner {
        /* 使input框的背景变透明 */
        background-color: transparent;
        /* 使边框也变透明 */
        border-color: transparent;
        /* 给边框加阴影能够使其有立体感 */
        box-shadow: 3px 3px 0 0 rgba(0, 0, 0, 0.2);
        /* 改变获取焦点后的竖线颜色 */
        caret-color: #3F5EFB;
        color: black;
    }

    .el-input__inner:hover {
        border-color: black;
    }

    .el-input__inner:focus {
        border-color: black;
    }

    .el-input__inner::placeholder {
        color: #3F5EFB;
    }

    .el-button {
        /* 使边框也变透明 */
        border-color: transparent;
        /* 给边框加阴影能够使其有立体感 */
        box-shadow: 3px 3px 0 0 rgba(0, 0, 0, 0.2);
        /* 改变获取焦点后的竖线颜色 */
        caret-color: #3F5EFB;
        color: black;
    }
</style>