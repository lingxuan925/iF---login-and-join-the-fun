# iF — login and join the fun

Welcome to our open source Android app! "iF" is a convenient event management app, although it is still a prototype, it's main functionality is implemented. 

"iF" helps event creator manage their events as well as check event participant's information online. For users, they can search nearby events, search events in a particular area or search by both category and area (You can be both event creator and participant). Users can only login through their Google account so far. When logged in, users can change their nickname, gender, avatar and public and private info.

For convenience, we used some open source libraries:
```
implementation 'de.hdodenhof:circleimageview:2.2.0'
implementation 'com.github.yalantis:ucrop:2.2.2'
implementation 'com.fxn769:pix:1.2.5'
```
The instruction of these libraries can be found here:

https://github.com/hdodenhof/CircleImageView

https://github.com/Yalantis/uCrop

https://github.com/akshay2211/PixImagePicker

We used google Api client and Firebase to support some functions in our app.

## Features
- Google login logout
- Change avatar (from camera or alberm, crop new avatar image)
- Change nickname, gender, "what's up", age
- Change search radius on "Settings" page (we will add more settings such as change theme, change personal interests)
- Send email to us on "FeedBack" page
- Search events in a specific area
- Show events nearby
- go to current location
- Switch between map view and list view
- Show events by category (Entertainment, Foodies, Sports, Travel) within your chosen radius
- Unjoin an events (if you are the event creator, you can only unjoin it when there are no participants in your event. After the creator unjoined, this event is deleted)
- Show event info(event name, description, number of participants, number of participants capacity, address, date and time) as well as creator info (avatar, name) when you click on the event icon on map, list or upcomming events.
- Show creator info in detail (name, age, gender, "what's up") when you click on the creator's avatar
- Send email to the creator on the creator info detail page
- Outdated event will not show on the map and MyEvents anymore, but there is a history in our database(Firebase)

## Graphic examaple

![](20190104_174023.gif)
Go to current location and switch of map view and list view

![](20190104_174143.gif) 
![](20190104_174325.gif)
Add a new event and it will be shown on map, if you forget to fill some of the fields, it will remind you.

![](20190104_174500.gif)
The UI design of popup event info page, when you click on the avatar, you can see the creator's info, you can also send a email directly to the creator

![](20190104_184136.gif)
![](20190104_184257.gif)
I have unjoined the last event, let create a new one and let me show you the login logout page.

## Last statement

/* This project was originally our final project of Android development course.

   I used some random pictures from internet, but most of them are from Alibaba vector image library:

   https://www.iconfont.cn/
   
   If there is any violation of your right, just let me know, we will remove them as soon as possible.
   
   My email address: zhatiayua59@gmail.com

