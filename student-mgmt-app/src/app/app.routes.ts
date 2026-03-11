import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { StudentList } from './components/student-list/student-list';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: Login
    },
    {
        path: 'dashboard',
        component: Dashboard,
        data: { roles: ['Admin'] },
        canActivate: [authGuard]
    },
    {
        path: 'students',
        component: StudentList,
        data: { roles: ['Admin', 'Staff'] },
        canActivate: [authGuard]
    }
];
