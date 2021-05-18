import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdvertPageComponent } from './components/advert-page/advert-page.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'advert/:id', component: AdvertPageComponent },
  // { path: 'XXX', component: RegisterComponent, canActivate: [AuthorizeGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
