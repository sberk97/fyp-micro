import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { CookieService } from 'ngx-cookie-service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AdvertPageComponent } from './components/advert-page/advert-page.component';
import { UniversalAppInterceptor } from './services/universal-app-interceptor/universal-app-interceptor-service.service';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SearchPageComponent } from './components/search-page/search-page.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { AdvertsGridComponent } from './components/adverts-grid/adverts-grid.component';
import { SearchBarComponent } from './components/search-bar/search-bar.component';
import { AdvertManageButtonsComponent } from './components/advert-manage-buttons/advert-manage-buttons.component';
import { AddAdvertPageComponent } from './components/add-advert-page/add-advert-page.component';
import { AuthorizeGuard } from './services/authorize-guard/authorize-guard.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    AdvertPageComponent,
    NotFoundPageComponent,
    HomePageComponent,
    SearchPageComponent,
    UserPageComponent,
    AdvertsGridComponent,
    SearchBarComponent,
    AdvertManageButtonsComponent,
    AddAdvertPageComponent,
  ],
  imports: [BrowserModule, FormsModule, HttpClientModule, AppRoutingModule],
  providers: [
    CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UniversalAppInterceptor,
      multi: true,
    },
    AuthorizeGuard,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
