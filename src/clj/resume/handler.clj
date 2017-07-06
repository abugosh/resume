(ns resume.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [resume.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]
            [amazonica.aws.simpleemail :as ses])
  (:use [ring.util.anti-forgery]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:title "Alex Bugosh"]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn layout [page & {:keys [info error]}]
  (html5
   (head)
   [:body
    [:div {:class "container-fluid" :role "main"}
     [:div {:class "row"}
      [:nav {:class "navbar navbar-default visible-xs-block visible-sm-block hidden-print"}
       [:div {:class "navbar-header"}
        [:button {:type "button" :class "navbar-toggle collapsed" :data-toggle "collapse" :data-target "#collapsed-menu" :aria-expanded "false"}
         [:span {:class "sr-only"} "Toggle Navigation"]
         [:span {:class "icon-bar"}]
         [:span {:class "icon-bar"}]
         [:span {:class "icon-bar"}]]
        [:a {:class "navbar-brand" :href "#"} "Alex Bugosh"]]
       [:div {:class "collapse navbar-collapse" :id "collapsed-menu"}
        [:ul {:class "nav navbar-nav"}
         [:li [:a {:href "/about"} "About"]]
         [:li [:a {:href "/resume"} "Resume"]]
         [:li [:a {:href "/contact"} "Contact"]]
         [:li
          [:ul {:class "list-inline" :id "nav-social-list"}
           [:li [:a {:target "_blank" :href "https://www.linkedin.com/in/abugosh"} [:i {:class "fa fa-linkedin"}]]]
           [:li [:a {:target "_blank" :href "https://twitter.com/abugosh"} [:i {:class "fa fa-twitter"}]]]
           [:li [:a {:target "_blank" :href "https://github.com/abugosh"} [:i {:class "fa fa-github"}]]]
           [:li [:a {:href "mailto:alex@alexbugosh.com"} [:i {:class "fa fa-envelope"}]]]]]]]]
      [:div {:class "col-md-2 visible-md-block visible-lg-block hidden-print" :id "menu"}
       [:h2 {:id "title"} [:a {:href "/"} "Alex Bugosh"]]
       [:br]
       [:ul {:class "list-unstyled"}
        [:li [:h5 [:a {:href "/about"} "About"]]]
        [:li [:h5 [:a {:href "/resume"} "Resume"]]]
        [:li [:h5 [:a {:href "/contact"} "Contact"]]]]
       [:br]
       [:ul {:class "list-inline"}
        [:li [:a {:target "_blank" :href "https://www.linkedin.com/in/abugosh"} [:i {:class "fa fa-linkedin"}]]]
        [:li [:a {:target "_blank" :href "https://twitter.com/abugosh"} [:i {:class "fa fa-twitter"}]]]
        [:li [:a {:target "_blank" :href "https://github.com/abugosh"} [:i {:class "fa fa-github"}]]]
        [:li [:a {:href "mailto:alex@alexbugosh.com"} [:i {:class "fa fa-envelope"}]]]]]
      [:div {:class "col-md-9" :id "main"}
       [:p {:class "alert alert-info" :role "alert"} info]
       [:p {:class "alert alert-danger" :role "alert"} error]
       page]]]
    (include-js "https://code.jquery.com/jquery-1.11.3.min.js")
    (include-js "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
    (include-js "/js/app.js")]))

(defn about []
  (list [:div {:class "row" :id "about"}
   [:h3 "Hi, I'm Alex!"]
   [:p "I'm a security engineer with a background in maintaining, developing and securing web applications. I have a great deal of experience with Ruby on Rails, and have spent the last year working with Rails codebases doing feature development and security. I've also worked down at the firmware level by developing security enhancements for Android platforms, and even done devops and systems administration for remote mission critical systems."]
   [:p "Today I'm doing security architecture. My days include creating security policies, performing manual code audits and security tests on internal applications, and advising teams to develop solutions to secure our systems. I’ve worked on moving a monolithic Rails application into an AWS based service-oriented architecture. I’ve also resolved internal application plumbing problems, including developing tools to enable engineers to quickly profile their code, upgrading Rails, and identifying/ fixing security issues. In my spare time, I'm exploring "
    [:a {:target "_blank" :href "https://clojure.org"} "Clojure"]
    " and using it to learn more about cryptography through "
    [:a {:target "_blank" :href "https://cryptopals.com"} "cryptopals"]
    "."]
   [:p "When I'm away from computers I've been known to climb rocks, race cars, and read books."]
   [:p "I'm based in Chicago and while I'm not currently looking for a job, "
    [:a {:href "/contact"} "contact me"]
    " if you'd like to chat!"]]
   [:div {:class "row"}
    [:div {:class "col-md-8 col-md-offset-2"}
     [:img {:class "img-responsive img-rounded" :alt "Driving!" :src "/images/driving.jpg"}]]]))

(defn resume []
  (list [:div {:class "row"}
         [:h1 {:class "text-center"} "Alex Bugosh"
          [:br]
          [:small "alex@alexbugosh.com"]]]
        [:div {:class "row"}
         [:h3 "Experience"]
         [:hr]
         [:h4 "Information Security Lead at Jellyvision"
          [:small "March 2017 - Present"]]
         [:hr]
         [:h4 "Tech Lead Platform at Jellyvision "
          [:small "September 2016 - March 2017"]]
         [:hr]
         [:h4 "Senior Software Engineer at Avant "
          [:small "January 2016 - July 2016"]]
         [:ul
          [:li "Upgraded Avant&#39;s large monolithic Rails application from Rails 3.2 to Rails 4.2"]
          [:li "Developed framework for doing performance tuning in Rails applications using profilers and ActiveSupport::Notifications"]
          [:li "Designed and prototyped a service to service authentication solution for future SOA development"]
          [:li "Managed Avant&#39;s bug bounty program"]
          [:li "Analyzed database for sensitive data and worked with teams to remove or encrypt the sensitive data"]
          [:li "Developed Elixir proof of concept finance calucator and gave several internal presentations on Elixir"]]
         [:hr]
         [:h4 "Senior Software Engineer at Effective Software Solutions, Inc. "
          [:small "March 2015 - December 2016"]]
         [:ul
          [:li "Implemented compression improvements improving bandwidth usage by 30% using C++"]
          [:li "Improved build and rebuild times by updating build targets across the project Automake build system"]
          [:li "Developed test framework for remote sensor platform using C++"]
          [:li "Supported team transition for node.js project that served as user interface for remote sensor platform"]
          [:li "Worked as a part of an agile SCRUM team"]]
         [:hr]
         [:h4 "Senior Software Engineer at PROTEUS Technologies "
          [:small "June 2014 - March 2015"]]
         [:h5 "Project D "
          [:small "August 2014 - March 2015"]]
         [:ul
          [:li "Developed user stories and api documents for system"]
          [:li "Developed prototype user survey tool using Ruby on Rails"]]
         [:h5 "IMC Project "
          [:small "June 2014 - October 2014"]]
         [:ul
          [:li "Co-authored winning IMC proposal"]
          [:li "Modified Android Booloader to include security enhancements"]
          [:li "Presented findings to customer"]]
         [:hr]
         [:h4 "Senior Cyber Engineer at Raytheon "
          [:small "August 2013 - June 2014"]]
         [:ul
          [:li "Performed vulnerability analysis of various applications using a combination of custom fuzzing frameworks and IDA Pro"]
          [:li "Performed vulnerability analysis on a micro controller platform, successfully reverse engineered data encoding method on target"]
          [:li "Performed code audits on open source products"]
          [:li "Developed extensions to custom analysis platform using C and used data to enhance IDA databases using IDAPython"]
          [:li "Performed research on static code analysis with a focus on finding methods of enhancing static analysis capabilities with data from dynamic program runs"]
          [:li "Developed document database with a Backbone.js and Bootstrap front-end, Django middleware, and programmaticly utilized a combination of Git and MySQL for storage"]]
         [:hr]
         [:h4 "Software Engineer at PROTEUS Technologies "
          [:small "July 2010 - August 2013"]]
         [:h5 "Security Research "
          [:small "January 2013-August 2013"]]
         [:ul
          [:li "Developing fuzzing capabilities for the Android platform"]
          [:li "Working with and building upon the MWR Mercury Framework"]
          [:li "Assisted with development of internal prototype capabilities"]]
         [:h5 "Project C Software Engineer "
          [:small "January 2013-August 2013"]]
         [:ul
          [:li "Developed QRC prototype capabilities using JRuby, Ruby on Rails, jQuery, Backbone.js, Redis, Bootstrap, and Oracle"]
          [:li "Developed administration tools for maintaining systems distributed across enterprise and off-site non-enterprise locations using Ruby, Python, and Bash"]
          [:li "Developed processes and tools for deploying systems at off-site non-enterprise locations"]
          [:li "Enhanced and expanded an existing Java Spring web application"]
          [:li "Deployed systems and tools to off-site non-enterprise locations"]]
         [:h5 "Project B Software Engineer "
          [:small "February 2012-January 2013"]]
         [:ul
          [:li "Maintained, updated, and refactored a Ruby on Rails application with a MySQL database"]
          [:li "Administered 25+ virtual servers distributed across the enterprise"]
          [:li "Developed Puppet scripts for automating deployments across enterprise locations"]
          [:li "Maintained, deployed, and setup CM tools for the team such as Gitlab, Jenkins, and RVM:FW"]
          [:li "Expanded the project&#39;s utilization of Redis as a caching and queuing tool to enhance application performance"]]
         [:h5 "Project A Software Engineer "
          [:small "September 2010-February 2012"]]
         [:ul
          [:li "Developed Ruby on Rails application for displaying, correlating, and searching analytics data using JRuby, Ruby on Rails, and Oracle"]
          [:li "Developed framework for building Javascript based widgets within the Ozone Widget Framework"]
          [:li "Developed and designed QRC capabilities using Ruby on Rails, JQuery, and Backbone.js"]
          [:li "Developed and designed processes for ingesting and tracking ingested data through its full lifecycle"]
          [:li "Worked as a part of a agile SCRUM team"]
          [:li "Wrote user stories and conducted research through user interviews"]]
         [:hr]
         [:h4 "Software Development Intern at Right Reason Technologies "
          [:small "January 2010 - June 2010"]]
         [:hr]
         [:h4 "Research Assistant at East Stroudsburg University "
          [:small "June 2009 - May 2010"]]
         [:ul
          [:li "Developed a Steganalysis framework for detecting hidden signals in videos using machine learning techniques"]
          [:li "Developed a fault tolerant Java program which interfaces with a modified version of FFMPEG in order to generate test data"]
          [:li "Led software engineering efforts and administered design, implementation and management of the project’s PostgreSQL database"]]
         [:hr]
         [:h4 "Founder at Startup "
          [:small "March 2009 - July 2009"]]
         [:hr]
         [:h4 "Web Developer at Wireless Solutions International "
          [:small "September 2007 - January 2009"]]
         [:hr]]
        [:div {:class "row"}
         [:h3 "Skills"]
         [:table {:class "table"}
          [:thead
           [:tr
            [:th "Languages"]
            [:th "Web Technologies"]
            [:th "Other"]]]
          [:tbody
           [:tr
            [:td "Ruby"]
            [:td "Ruby on Rails"]
            [:td "Git"]]
           [:tr
            [:td "Javascript"]
            [:td "Backbone.js"]
            [:td "Linux"]]
           [:tr
            [:td "Bash"]
            [:td "jQuery"]
            [:td "MySQL"]]
           [:tr
            [:td "Java"]
            [:td "Bootstrap"]
            [:td "PostgreSQL"]]
           [:tr
            [:td "C"]
            [:td "HTML/CSS"]
            [:td "Redis"]]
           [:tr
            [:td "C++"]
            [:td "REST"]
            [:td "Vim"]]
           [:tr
            [:td "Elixir"]
            [:td "AJAX"]
            [:td "Scrum"]]]]]
        [:div {:class "row"}
         [:h3 "Education"]
         [:hr]
         [:h4 "B.S. Computer Science and Computer Security from East Stroudsburg University of Pennsylvania "
          [:small "2007-2010"]]
         [:hr]]))

(defn contact []
  (list [:div {:class "row"}
         [:div {:class "jumbotron"}
          [:h3 "I&#39;d love to hear from you!"]
          [:p "I can be reached at alex@alexbugosh.com or using the form below!"]]]
        [:div {:class "row"}
         [:form {:class "form-horizontal", :action "/contact", :method "post"}
          [:div {:class "form-group"}
           [:label {:for "name", :class "col-md-1 control-label"} "Name"]
           [:div {:class "col-md-5"}
            [:input {:type "text", :name "name", :class "form-control", :id "name", :placeholder "Name"}]]]" "
          [:div {:class "form-group"}
           [:label {:for "email", :class "col-md-1 control-label"} "Email"]
           [:div {:class "col-md-5"}
            [:input {:type "email", :name "email", :class "form-control", :id "email", :placeholder "Email"}]]]" "
          [:div {:class "form-group"}
           [:label {:for "message", :class "col-md-1 control-label"} "Message"]
           [:div {:class "col-md-10"}
            [:textarea {:class "form-control", :name "message", :id "message", :placeholder "Message to Alex", :rows "5"}]]]" "
          (anti-forgery-field)
          [:button {:type "submit", :class "btn btn-default"} "Contact Alex!"]]]))

(defn send-email [name email message]
  (do
    (ses/send-email :destination {:to-addresses ["contact.form@alexbugosh.com"]}
                    :source "contact.form@alexbugosh.com"
                    :message {:subject (str "Website contact request from " name)
                              :body {:html (html5
                                            [:h1 "Contact Request!"]
                                            [:hr]
                                            [:p [:b "From:"] " " name]
                                            [:p [:b "Email:"] " " email]
                                            [:hr]
                                            [:p message]
                                            [:hr]
                                            [:b "Done!"])}})
    (layout (contact) :info "Email sent! I'll get back to you soon!")))

(defroutes routes
  (GET "/" [] (layout (about)))
  (GET "/about" [] (layout (about)))
  (GET "/resume" [] (layout (resume)))
  (GET "/contact" [] (layout (contact)))
  (GET "/redir" [] {:status 302 :headers {"Location" "/resume"}})
  (POST "/contact" [name email message] (send-email name email message))

  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
