import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdvertPageComponent } from './components/advert-page/advert-page.component';
import { LoginComponent } from './components/login/login.component';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page/not-found-page.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  { path: '', component: LoginComponent }, // to be changed to homepage
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'advert/:id', component: AdvertPageComponent },
  { path: 'not-found', component: NotFoundPageComponent },
  { path: '**', component: NotFoundPageComponent }, // Always last, wildcard route for a 404 page
  // { path: 'XXX', component: RegisterComponent, canActivate: [AuthorizeGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
