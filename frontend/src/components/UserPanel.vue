<template>
  <div class="user-panel">
    <h2>{{user.name}}</h2>
    <div class="roles">
      <template v-for="(role, index) in user.roles" :key="index">
        <ul class="role">
          <li>
            {{role.name}}
          </li>
        </ul>
      </template>
    </div>

    <div class="operate">
      <button class="edit" @click="handleEdit">Edit</button>
      <button class="delete" @click="handleDelete">Delete</button>
    </div>

    <Dialog v-model:show="showdialog">
      <div class="name">
        <input type="text" v-model="inputname"><br/>
        <div class="operate">
          <button class="submit" @click="handleChangeName">Change Name</button>
          <button class="cancel" @click="handleCancel">Cancel</button>
        </div>
      </div>

      <div class="roles">
        <template v-for="(role, index) in roles" :key="index">
          <div class="role">
            <input type="checkbox"
                   :value="role"
                   :disabled="disabled(role)"
                   v-model="selectdRoles"/>

            {{role.name}}
          </div>
        </template>
      </div>

      <div class="operate">
        <button class="submit" @click="handleChangeRoles">Change Role</button>
        <button class="cancel" @click="handleCancel">Cancel</button>
      </div>
    </Dialog>
  </div>
</template>

<script lang="ts" setup>
import {computed, ref} from "vue";
import Dialog from "@/components/Dialog.vue";

const props = defineProps<{
  user: User,
  roles: Role[]
}>()

const emits = defineEmits(["change-name", "change-roles", "ondelete"])
const showdialog = ref(false)
const checked = computed(() => {
  return (role: Role) => {
    return props.user.roles.findIndex(x => x.id == role.id) != -1
  }
})

const disabled = computed(() => {
  return (role: Role) => {
    return role.name == "user"
  }
})

const selectdRoles = ref<Role[]>(props.user.roles)
const inputname = ref(props.user.name)
function handleEdit() {
  showdialog.value = true
}

function handleChangeRoles() {
  emits("change-roles", selectdRoles.value)
  showdialog.value = false
}

function handleChangeName() {
  emits("change-name", inputname.value)
  showdialog.value = false
}

function handleCancel() {
  showdialog.value = false
}

function handleDelete() {
  emits("ondelete")
}
</script>

<style lang="scss" scoped>
div.user-panel {
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;

  height: 180px;
  text-align: left;

  div.operate {
    display: flex;
    justify-content: flex-start;
  }
}
</style>