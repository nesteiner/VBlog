<template>
  <div class="role-manage column">
    <div class="row">
      <div class="form-item">
        <input type="text" v-model="text" placeholder="输入新的角色名称" required/>
      </div>
      <button @click="handleCreate">新建</button>
    </div>

    <table>
      <tr>
        <th>id</th>
        <th>名称</th>
        <th>操作</th>
      </tr>

      <template v-for="(role, index) in roles" :key="index">
        <tr>
          <td>{{role.id}}</td>
          <td>{{role.name}}</td>
          <td>
            <div class="row">
              <button @click="handleEdit(role, index)">编辑</button>
              <button @click="handleDelete(index)">删除</button>
            </div>
          </td>
        </tr>
      </template>
    </table>

    <Dialog v-model:show="showDialogEdit">
      <div class="form" v-if="currentRole != null">
        <div class="form-item">
          <input type="text" v-model="updateText"/>
        </div>

        <div class="form-item">
          <div class="row">
            <button @click="handleEditCancel">取消</button>
            <button @click="handleEditConfirm">确定</button>
          </div>
        </div>
      </div>
    </Dialog>

    <Dialog v-model:show="showDialogDelete">
      <div class="alert-dialog">
        <div class="alert">
          <h1>确定要删除这个角色?</h1>
        </div>

        <div class="row">
          <button @click="handleDeleteCancel">取消</button>
          <button @click="handleDeleteConfirm">确定</button>
        </div>
      </div>
    </Dialog>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {deleteRole, findRoles, insertRole, updateRole} from "@/api";
import {Dialog} from "@/components/dialog"

const roles = ref<Role[]>([])
const currentRole = ref<Role | null>(null)
const currentIndex = ref(0)
const text = ref("")
const updateText = ref("")
const showDialogEdit = ref(false)
const showDialogDelete = ref(false)

async function handleCreate() {
  let role = await insertRole({name: text.value})
  roles.value.push(role)
  text.value = ""
}

function handleEdit(role: Role, index: number) {
  currentRole.value = role
  currentIndex.value = index
  showDialogEdit.value = true
}

function handleDelete(index: number) {
  currentIndex.value = index
  showDialogDelete.value = true
}

function handleEditCancel() {
  showDialogEdit.value = false
}

async function handleEditConfirm() {
  let id = roles.value[currentIndex.value].id
  let name = updateText.value

  roles.value[currentIndex.value] = await updateRole({id, name})
  showDialogEdit.value = false
  updateText.value = ""
}

function handleDeleteCancel() {
  showDialogDelete.value = false
}

async function handleDeleteConfirm() {
  let id = roles.value[currentIndex.value].id
  await deleteRole(id)
  roles.value.splice(currentIndex.value, 1)

  showDialogDelete.value = false
}

onMounted(async () => {
  roles.value = await findRoles()
})
</script>

<style lang="scss" scoped>
div.role-manage.column {
  display: flex;
  flex-direction: column;
  align-items: center;

  div.row {
    width: 100%;
    display: flex;
    justify-content: center;
  }

  div.form-item {
    div.row {
      justify-content: flex-end;
    }
  }

  table {
    td {
      word-break: break-all;
    }
  }
}
</style>