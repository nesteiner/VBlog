import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {createPinia} from "pinia";

import "scratch-components/lib/style.css"
import "element-plus/dist/index.css"
const pinia = createPinia()

createApp(App).use(router).use(pinia).mount('#app')
