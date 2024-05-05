import MainPage from "@/components/MainPage.vue";
import {createRouter, createWebHistory} from "vue-router";
import OfferDetails from "@/components/search/OfferDetails.vue";
import AllOffers from "@/components/debug/AllOffers.vue";
import AllFlights from "@/components/debug/AllFlights.vue";
import AllHotels from "@/components/debug/AllHotels.vue";

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
    },
    {
        path: '/offers',
        name: 'AllOffers',
        component: AllOffers
    },
    {
        path: '/flights',
        name: 'AllFlights',
        component: AllFlights
    },
    {
        path: '/hotels',
        name: 'AllHotels',
        component: AllHotels
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router