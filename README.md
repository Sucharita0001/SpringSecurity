# SpringSecurity

This application for spring security.

> ## Work done till now
> > Currently using In Memory User Detail is using currently.

> ## For running the application
> > Use configuration as
> >
> > ADMIN_AUTHORITY=admin;ADMIN_NAME=admin;ADMIN_PASSWORD={bcrypt}<bcrypt_encoded_user_password>.;BASIC_USER_AUTHORITY=read;BASIC_USER_NAME=user;BASIC_USER_PASSWORD={bcrypt}<bcrypt_encoded_admin_password>
> >
> > For encoding password we can use link https://bcrypt-generator.com/

> ## Endpoints
> > ### Appliction base URL
> > > http://localhost:10
> > ### Open endpoints
> > > /actuator, /open
> > ### Restricted endpoints
> > > /restricted

> ## If password is not strong, it will give error as 
> ![img.png](img.png)