<template>
  <div class="user-manage">
    <Row class="header" main-axis-aligment="center">
      <el-input placeholder="默认展示部分用户，可以通过用户名搜索用户" :prefix-icon="Search"
                v-model="keywords" size="small"/>
      <el-button type="primary" :icon="Search" size="small" @click="searchClick">搜索</el-button>
    </Row>

    <Row class="body" main-axis-aligment="space-around">
      <template v-for="(user, index) in users" :key="index">
        <el-card v-loading="cardloading[index]">
          <template #header>
            <div class="nickname">
              <span>{{ user.nickname }}</span>
              <el-button :icon="Delete" @click="deleteUser(user.id)" :disabled="username == user.name">删除</el-button>
            </div>
          </template>

          <div class="info">
            <!-- <div class="userface"><img :src="user.userface" :alt="user.nickname"/></div> -->
            <Center class="userface">
              <ImageUpload :default-url="user.userface" @input="onInputImage(user, $event)"/>
            </Center>

            <div class="name">
              <span>用户名:</span>
              <span>{{ user.name }}</span>
            </div>

            <div class="email">
              <span>电子邮箱:</span>
              <span>{{ user.email }}</span>
            </div>

            <div class="register-time">
              <span>注册时间:</span>
              <span>{{ user.registerTime }}</span>  
            </div>
          </div>

          <Row class="status" cross-axis-aligment="center">
            <span>用户状态</span>
            <el-switch v-model="user.enabled" active-text="启用" active-color="#13ce66"
                       :disabled="username == user.name"
                       @change="enabledChange(user.enabled, user.id, index)" 
                       inactive-text="禁用"/>
          </Row>

          <Row class="roles" cross-axis-aligment="center">
            <span>用户角色</span>
            <template v-for="role in user.roles" :key="role.id">
              <el-tag size="small" type="success">{{ role.name }}</el-tag>
            </template>
            
            <el-popover placement="right" :width="200" @hide="saveRoles(user.id, index)"
                        :disabled="username == user.name"
                        trigger="click">
              
                <el-select v-model="roleids" :key="user.id" multiple placeholder="请选择" size="small">
                  <template v-for="item in roles" :key="`${item.id}-${user.id}`">
                    <el-option :label="item.name" :value="item.id"/>
                  </template>
                </el-select>

                <template #reference>
                  <el-button type="default" :icon="More" @click="showRole(user.roles, user.id, index)"/>
                </template>
            </el-popover>
          
          </Row>
        </el-card>
      </template>
    </Row>

    <FloatingActionButton :on-pressed="onPressed"/>

    <el-dialog v-model="showDialog" title="添加用户">
      <el-form :model="registerData">
        <el-form-item label="名字">
          <el-input v-model="registerData.name"/>
        </el-form-item>

        <el-form-item label="角色">
          <el-select v-model="registerData.roles" placeholder="please select your roles" multiple>
            <template v-for="(role, index) in roles" :key="index">
              <el-option :label="role.name" :value="role"/>
            </template>
          </el-select>
        </el-form-item>

        <el-form-item label="密码">
          <el-input type="password" placeholder="input your password" v-model="registerData.passwordHash"/>
        </el-form-item>

        <el-form-item label="昵称">
          <el-input placeholder="input your nickname" v-model="registerData.nickname"/>
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input type="email" placeholder="input your email" v-model="registerData.email"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">Create</el-button>
          <el-button @click="onCancel">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { changeAvatar, currentUsername, deleteUserByAdmin, findAllUsersByNickname, findRoles, register, updateRoles, updateUserEnabled, uploadImage } from '@/api';
import { ElMessage, vLoading, ElInput, ElButton, 
  ElCard, ElSwitch, ElPopover, ElSelect, ElTag,
  ElOption, ElDialog, ElForm, ElFormItem } from 'element-plus';
import {Search, Delete, More} from "@element-plus/icons"
import { Center, Row } from 'scratch-components';
import { ref, onMounted, reactive } from 'vue';
import {FloatingActionButton} from "@/components/floating-action-button"
import ImageUpload from './ImageUpload.vue';

const loading = ref(false)
const eploading = ref([] as boolean[])
const cardloading = ref([] as boolean[])
const keywords = ref("")
const users = ref([] as User[])
const roles = ref([] as Role[])
const roleids = ref([] as number[])
const cproles = ref([] as Role[])
const username = ref("")

const showDialog = ref(false)
const defaultRegisterData: RegisterUserRequest = {
  name: "",
	roles: [] as Role[],
	passwordHash: "",
	nickname: "",
	email: "",
	userface: "http://localhost/api/image/download/1"
}

const registerData = ref<RegisterUserRequest>(defaultRegisterData)

const saveRoles = async (id: number, index: number) => {
  if (cproles.value.length == roleids.value.length) {
    for (let index1 = 0; index1 < cproles.value.length; index1 += 1) {
      for (let index2 = 0; index2 < roleids.value.length; index2 += 1) {
        if (cproles.value[index1].id == roleids.value[index2]) {
          roleids.value.splice(index2, 1)
          break
        }
      }
    }

    if (roleids.value.length == 0) {
      return
    }
  }

  cardloading.value[index] = true
  try {
    let roles = await updateRoles({
      userid: id,
      roleids: roleids.value
    })

    let index = users.value.findIndex((item) => item.id == id)
    if (index != -1) {
      users.value[index].roles = roles
    }
  } catch (error: any) {
    ElMessage.error("更新失败")
  } finally {
    cardloading.value[index] = false
  }
}

const showRole = async (_roles: Role[], id: number, index: number) => {
  cproles.value = _roles
  roleids.value = []
  await loadRoles(index)
  for (let index = 0; index < _roles.length; index += 1) {
    roleids.value.push(_roles[index].id)
  }
}

const deleteUser = async (id: number) => {
  let flag = confirm("确认删除该用户")
  loading.value = true
  
  if (flag) {
    try {
      await deleteUserByAdmin(id)
      let index = users.value.findIndex((item) => item.id == id)
      if (index != -1) {
        users.value.splice(index, 1)
      }

      ElMessage.success("删除成功")
    } catch (error: any) {
      ElMessage.error("删除失败")
    } finally {
      loading.value = false
    }

  }

  loading.value = false
}

const enabledChange = async (enabled: boolean, id: number, index: number) => {
  cardloading.value[index] = true
  try {
    await updateUserEnabled({
      userid: id,
      enabled
    })

    let index = users.value.findIndex((item) => item.id == id)
    if (index != -1) {
      users.value[index].enabled = enabled
    }

    ElMessage.success("更新成功")
  } catch (error: any) {
    ElMessage.error("更新失败")
  } finally {
    cardloading.value[index] = false
  }
}

const loadRoles = async (index: number) => {
  eploading.value[index] = true
  try {
    roles.value = await findRoles()
  } catch (error: any) {
    ElMessage.error("数据加载失败")
  } finally {
    eploading.value[index] = false
  }
}

const loadUsers = async () => {
  try {
    users.value = await findAllUsersByNickname(keywords.value)
  } catch (error: any) {
    ElMessage.error("数据加载失败")
  } finally {
    loading.value = false
  }
}

const searchClick = async () => {
  loading.value = true
  await loadUsers()
}

const onPressed = () => {
  registerData.value = defaultRegisterData
  showDialog.value = true
}

const onSubmit = async () => {
  try {
    let user = await register(registerData.value)
    users.value.push(user)
  } catch (error: any) {
    ElMessage.error("添加用户失败")
  } finally {
    showDialog.value = false
  }
  
}

const onCancel = () => {
  showDialog.value = false
}

const onInputImage = async (user: User, file: File) => {
  try {
    let imageitem = await uploadImage(file)
    user.userface = `http://localhost/api/image/download/${imageitem.id}`
    await changeAvatar(user.id, user.userface)
  } catch (error: any) {
    ElMessage.error("更新头像失败")
  }
}

onMounted(async () => {
  loading.value = true
  await loadUsers()
  cardloading.value = Array.apply(null, Array(20)).map((item, i) => false)
  eploading.value = Array.apply(null, Array(20)).map((item, i) => false)
  roles.value = await findRoles()
  username.value = await currentUsername()
})
</script>

<style lang="scss" scoped>
.header {
  margin-top: 10px;
}

.body {
  flex-wrap: wrap;

  .el-card {
    width: 330px;
    margin-top: 10px;
  }

  .nickname {
    text-align: left;

    .el-button {
      float: right;
      padding: 3px;
      color: #ff0509;
    }
  }

  .info {
    .name {
      text-align: left;    
      color: #20a0ff;
      font-size: 12px;
      margin-top: 13px;
    }

    .email {
      text-align: left;
      color: #20a0ff;
      font-size: 12px;
      margin-top: 13px;
    }

    .register-time {
      text-align: left;
      color: #20a0ff;
      font-size: 12px;
    }

    .status {
      text-align: left;
      color: #20a0ff;
      font-size: 12px;
      margin-top: 13px;
    }

    .roles {
      text-align: left;
      color: #20a0ff;
      font-size: 12px;
      margin-top: 13px;

      .el-popover {
        .el-button {
          padding-top: 0px;
        }
      }
    }
    
  }
}
</style>