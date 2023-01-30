<template>
  <div class="student-manage column">
    <div class="row">
      <button @click="handleCreate">新建</button>
    </div>

    <table>
      <tr>
        <th>id</th>
        <th>name</th>
        <th>grade</th>
        <th>major</th>
        <th>clazz</th>
        <th>institute</th>
        <th>telephone</th>
        <th>email</th>
        <th>sex</th>
        <th>身份证</th>
        <th>roles</th>
        <th>操作</th>
      </tr>

      <template v-for="(student, index) in students" :key="index">
        <tr>
          <td>{{ student.id }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.grade }}</td>
          <td>{{ student.major }}</td>
          <td>{{ student.clazz }}</td>
          <td>{{ student.institute }}</td>
          <td>{{ student.telephone }}</td>
          <td>{{ student.email }}</td>
          <td>{{ student.sex }}</td>
          <td>{{student.cardId}}</td>
          <td>{{ student.roles.map(x => x.name).join(",") }}</td>
          <td>
            <div class="row">
              <button @click="handleEdit(student, index)">编辑</button>
              <button @click="handleDelete(index)">删除</button>
            </div>
          </td>
        </tr>
      </template>
    </table>

    <Pagination :current-page="currentPage"
                :page-size="pageSize"
                :total-pages="totalPages"/>

    <Dialog v-model:show="showCreateDialog" class="create">
      <div class="form">
        <div class="form-item">
          <span>学生名称</span>
          <input type="text" v-model="inputStudent.name" placeholder="输入学生名字" required/>
        </div>

        <div class="form-item">
          <span>登录密码</span>
          <input type="password" v-model="inputStudent.passwordHash" placeholder="输入登录密码" required/>
        </div>

        <div class="form-item">
          <span>学生年级</span>
          <input type="text" v-model="inputStudent.grade" placeholder="输入学生年级" required/>
        </div>

        <div class="form-item">
          <span>学生专业</span>
          <input type="text" v-model="inputStudent.major" placeholder="输入学生专业" required/>
        </div>

        <div class="form-item">
          <span>学生班级</span>
          <input type="text" v-model="inputStudent.clazz" placeholder="输入学生班级" required/>
        </div>

        <div class="form-item">
          <span>学生学院</span>
          <input type="text" v-model="inputStudent.institute" placeholder="输入学生学院" required/>
        </div>

        <div class="form-item">
          <span>学生电话</span>
          <input type="tel" v-model="inputStudent.telephone" placeholder="输入学生电话" required/>
        </div>

        <div class="form-item">
          <span>学生邮箱</span>
          <input type="email" v-model="inputStudent.email" placeholder="输入学生邮箱" required/>
        </div>

        <div class="form-item">
          <span>学生身份证</span>
          <input type="email" v-model="inputStudent.cardId" placeholder="输入学生身份证" required/>
        </div>

        <div class="form-item">
          <span>学生性别</span>
          <select v-model="inputStudent.sex">
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>

        <div class="form-item">
          <button @click="handleCreateCancel">取消</button>
          <button @click="handleCreateConfirm">确定</button>
        </div>
      </div>
    </Dialog>

    <Dialog v-model:show="showEditDialog" class="edit">
      <div class="form" v-if="currentStudent != null">
        <div class="form-item">
          <span>学生名称</span>
          <input type="text" v-model="currentStudent.name" placeholder="输入学生名字" required/>
        </div>

        <div class="form-item">
          <span>登录密码</span>
          <input type="password" v-model="newPassword" placeholder="输入登录密码" required/>
        </div>

        <div class="form-item">
          <span>学生年级</span>
          <input type="text" v-model="currentStudent.grade" placeholder="输入学生年级" required/>
        </div>

        <div class="form-item">
          <span>学生专业</span>
          <input type="text" v-model="currentStudent.major" placeholder="输入学生专业" required/>
        </div>

        <div class="form-item">
          <span>学生班级</span>
          <input type="text" v-model="currentStudent.clazz" placeholder="输入学生班级" required/>
        </div>

        <div class="form-item">
          <span>学生学院</span>
          <input type="text" v-model="currentStudent.institute" placeholder="输入学生学院" required/>
        </div>

        <div class="form-item">
          <span>学生电话</span>
          <input type="tel" v-model="currentStudent.telephone" placeholder="输入学生电话" required/>
        </div>

        <div class="form-item">
          <span>学生邮箱</span>
          <input type="email" v-model="currentStudent.email" placeholder="输入学生邮箱" required/>
        </div>

        <div class="form-item">
          <span>学生身份证</span>
          <input type="email" v-model="currentStudent.cardId" placeholder="输入学生身份证" required/>
        </div>

        <div class="form-item">
          <span>学生性别</span>
          <select v-model="currentStudent.sex">
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>

        <div class="form-item">
          <button @click="handleEditCancel">取消</button>
          <button @click="handleEditConfirm">确定</button>
        </div>
      </div>
    </Dialog>

    <Dialog v-model:show="showDeleteDialog" class="delete">
      <div class="alert-dialog">
        <div class="alert">
          <h1>
            确定删除这个用户?
          </h1>
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
import {onMounted, ref, watch} from "vue";
import {Dialog} from "@/components/dialog/";
import {deleteUserByAdmin, findAllUser, registerStudent, updateStudent} from "@/api";
import {Pagination} from "@/components/pagination";

const totalPages = ref(0)
const currentPage = ref(1)
const pageSize = 10

const showCreateDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const defaultInputStudent: RegisterStudentRequest = {
  name: "",
  grade: "",
  major: "",
  clazz: "",
  institute: "",
  telephone: "",
  email: "",
  passwordHash: "",
  sex: "男",
  cardId: ""
}

const inputStudent = ref<RegisterStudentRequest>(defaultInputStudent)

const students = ref<Student[]>([])
const currentStudent = ref<Student | null>(null)
const currentIndex = ref<number>(0)
// for update password
const newPassword = ref<string>("")
function handleCreate() {
  showCreateDialog.value = true
}

function handleEdit(student: Student, index: number) {
  currentStudent.value = {...student}
  currentIndex.value = index
  showEditDialog.value = true
}

async function handleDelete(index: number) {
  currentIndex.value = index
  showDeleteDialog.value = true
}

function handleCreateCancel() {
  inputStudent.value = defaultInputStudent
  showCreateDialog.value = false
}

async function handleCreateConfirm() {
  try {
    await registerStudent(inputStudent.value)
    let response: DataResponse<Student> = await findAllUser("student", currentPage.value - 1, pageSize)
    students.value = response["content"]
    totalPages.value = response["totalPages"]

  } catch (error: any) {
    alert(error.response.data["message"])
  } finally {
    showCreateDialog.value = false
  }

}

function handleEditCancel() {
  showEditDialog.value = false
}

async function handleEditConfirm() {
  if (currentStudent.value != null) {
    let request: UpdateStudentRequest = {
      ...currentStudent.value,
      passwordHash: newPassword.value
    }

    try {
      students.value[currentIndex.value] = await updateStudent(request)
    } catch (error: any) {
      alert(error.response.data["message"])
    } finally {
      showEditDialog.value = false
      newPassword.value = ""
    }
  }
}

function handleDeleteCancel() {
  showDeleteDialog.value = false
}

async function handleDeleteConfirm() {
  await deleteUserByAdmin(students.value[currentIndex.value].id)
  students.value.splice(currentIndex.value, 1)
}

watch(currentPage, async (newvalue, _) => {
  let response: DataResponse<Student> = await findAllUser("student", newvalue - 1, pageSize)
  students.value = response["content"]
  totalPages.value = response["totalPages"]
})

onMounted(async () => {
  let response = await findAllUser("student", currentPage.value - 1, pageSize)
  students.value = response["content"]
  totalPages.value = response["totalPages"]
})
</script>

<style lang="scss" scoped>
div.student-manage.column {
  display: flex;
  flex-direction: column;
  align-items: center;

  div.row {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: center;

  }

  table {
    width: 80%;

    td {
      word-break: break-all;

      div.row {
        display: flex;
        flex-direction: row;
        align-items: center;
      }
    }
  }

  div.form {
    display: flex;
    flex-direction: column;
    width: 100%;
    align-items: flex-end;
  }
}
</style>