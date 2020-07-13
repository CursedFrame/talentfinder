Original App Design Project - Nicholas Powell
===

# TalentFinder

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
TalentFinder is an application designed to match project managers (Finders) and auditioners (Creators). These roles are not mutually exclusive, meaning users can decide to create projects or join them. Users will either login or register at the start of the app and then navigate to the Home screen, where they can view a list of projects to participate in or create a project. This application will use the Facebook SDK to allow users to connect their Facebook profiles to their application profiles.


### App Evaluation
- **Category:** Social Networking, Business
- **Mobile:** Quick and easy way to upload media to audition for roles, on the go project updates and connection with other users.
- **Story:** Finding talent work is immensely important for both Creators and Finders, so providing a quicker and easier process will be very valuable to all users.
- **Market:** User base is similar to that of Fiverr when including amateur talent. Includes different types of talent: (voice acting, guitar, singing, etc.) Demo will not include actual users, only mock users.
- **Habit:** Not habit forming, app use should be on a need be basis.
- **Scope:** Technically challenging considering I am planning to create a database and parse info to and from it. Lots of different views are involved, user tags, project tags, etc. for matching Creator and Finder users.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can create a new account
* User can login
* User can use camera to post profile picture photo
* User can connect to Facebook (uses Facebook SDK)
* Users can direct message each other
* Finder user can post new project
* Creator user can post media files to database for Finder user project to recieve
* Users can see feed of available projects



**Optional Nice-to-have Stories**

* User can view other user's profiles and view projects they are a part of
* User can follow certain projects (video games, animation, etc.)
* User can use tags to filter projects/users
* Finder user can post project updates
* Users can see notifications on project updates (auditioners send audio, projecter managers send updates)
* Finder user can see feed of users

### 2. Screen Archetypes and Specific Screens

* Login
   * User can login
* Registration
   * User can create a new account
* Profile 
    * User can view own profile or other profiles
    * User can add profile photo using camera functionality
    * User can make connection to Facebook
* Detail (Project)
    * User can see a more detailed view of project
* Stream (Project Feed)
    * User can view a feed of projects or users
* Messaging (Direct Messages)
    * User can access direct messages to other users 
* Messaging (Start Discussion)
    * User can create message for another user
* Messaging (Discussion)
    * User can open discussion already started with other user
* Creation (Finder)
    * User can create a new project attached
* Creation (Creator)
    * User can create audition for specified project
* Search
    * User can search for certain projects or users


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Tab
    * Project Feed
* Messages Tab
    * Direct Messages
* Create Tab
    * Creation (Finder)
* Search Tab
    * Search

**Flow Navigation** (Screen to Screen)

* Login
   * Project Feed
* Registration
   * Project Feed
* Profile
    * Start Discussion
    * Project
    * **BACK**
* Project
    * Start Discussion
    * Profile
    * Creation (Creator)
    * **BACK**
* Project Feed
    * Profile
    * Project
    * **BACK**
* Direct Messages
    * Discussion
    * **BACK**
* Start Discussion
    * Discussion
    * **BACK**
* Discussion
    * **BACK**
* Creation (Finder)
    * Project Feed (After Project Creation)
* Creation (Creator)
    * Project (After Audition Creation)
* Search
    * Profile
    * Project
    * **BACK**


# **END UNTIL UNIT 9**

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
