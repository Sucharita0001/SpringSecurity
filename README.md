# SpringSecurity

This application for spring security.

> ## Work done till now
> > Currently, users are saved in h2-database files. After creating my user, using the valid credential restricted endpoint can be accessed.

~~> ## For running the application
> > Use configuration as
> >
> > ADMIN_AUTHORITY=admin;ADMIN_NAME=admin;ADMIN_PASSWORD={bcrypt}<bcrypt_encoded_user_password>.;BASIC_USER_AUTHORITY=read;BASIC_USER_NAME=user;BASIC_USER_PASSWORD={bcrypt}<bcrypt_encoded_admin_password>
> >
> > For encoding password we can use link https://bcrypt-generator.com/~~

> ## Endpoints
> > ### Appliction base URL
> > > http://localhost:10
> > ### Open endpoints
> > > /actuator, /open, /h2-console, /register
> > ### Restricted endpoints
> > > /restricted
> > ### Sample user register body 
> > > ![img_1.png](img_1.png) After this user creation restricted endpoint can be opened with valid credential (in this case user: user1@test.com, password: user1@#password)

> ## If password is not strong, it will give error as 
> ![img.png](img.png)