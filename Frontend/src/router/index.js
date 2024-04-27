import MainPage from "@/components/MainPage.vue";
import {createRouter, createWebHistory} from "vue-router";
import OfferDetails from "@/components/search/OfferDetails.vue";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: MainPage
    },
    {
        path: '/offer/:id',
        name: 'OfferDetails',
        component: OfferDetails
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router