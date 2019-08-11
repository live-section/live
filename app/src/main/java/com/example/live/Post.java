package com.example.live;


import java.util.ArrayList;
import java.util.Date;

public class Post {
    private String title;
    private String text;
    private String image;
    private String user;
    private Date date;
    private boolean expanded;
    // TODOD : support image and not url


    public Post(String title, String text, String image, String user, Date date) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.user = user;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getuser(){return user;}

    public Date getDate(){return date;}

    public String getImage() {
        return image;
    }

    public void setImage(String img) {
        this.image = img;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public static ArrayList<Post> createPostsList(int count) {
        ArrayList<Post> contacts = new ArrayList<Post>();

        ArrayList<String> texts = new ArrayList<String>();
        texts.add("What the fuck did you just fucking say about me, you little bitch? I'll have you know I graduated top of my class in the Navy Seals, and I've been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills. I am trained in gorilla warfare and I'm the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You're fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that's just with my bare hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little \"clever\" comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn't, you didn't, and now you're paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it. You're fucking dead, kiddo.");
        texts.add("Excuse me? I find vaping to be one of the best things in my life.  It has carried me through the toughest of times and brought light and vapor upon my spirit.  You're just another one of those people who doesn't believe in chem trails and fluoride turning us gay.  Your ignorance to the government is what makes you a sheep in today's society. Have fun being a slave to todays's system.\uFEFF");
        texts.add("I sexually Identify as the \"I sexually identify as an attack helicopter\" joke. Ever since I was a child, I've dreamed of flippantly dismissing any concepts or discussions regarding gender that don't fit in with what I learned in 8th grade bio. People say to me that this joke hasn't been funny since 2014 and please at least come up with a new one, but I don't care, I'm hilarious. I'm having a plastic surgeon install Ctrl, C, and V keys on my body. From now on I want you guys to call me \"epic kek dank meme trannies owned with facts and logic\" and respect my right to shit up social media. If you can't accept me you're a memeophobe and need to check your ability-to-critically-think privilege. Thank you for being so understanding.\n");
        texts.add("Hi there, I was really hoping that we could postpone the raid. I really want to see the aliens but I have to take my cat to the vet on September 20. I just really hope the government isn't forced to spend increasing amounts of money on defense while we keep postponing. Maybe postponing will give us more time to plan our alien parties! Postponing could even give more time for more people to sign up, while we take our time to figure out a day that better works for everyone, but lets keep up the planning please!");

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("What the Government Doesn't Want You to Know About Gay");
        titles.add("10 Ways Marketers Are Making You Addicted to Gay");
        titles.add("7 Things Kim Kardashian Has in Common With Android");
        titles.add("Try Not To Dig Your Fingernails Into Your Desk When You Listen To The Second Half Of The Third Sentence.");
        titles.add("If You Can Watch This And Not Feel Surprised, Then You Are Made Of Ice.");
        titles.add("Before You Say Babies Can't Be Homophobic, Listen To These Two Questions From A NBA Coach.");

        ArrayList<String> images = new ArrayList<String>();
        images.add("http://i.imgur.com/DvpvklR.png");
        images.add("https://i.imgur.com/8075uS2.jpg");
        images.add(null);
        images.add("https://i.imgur.com/YVXAlZF.jpg");
        images.add("https://i.imgur.com/pu7mumm.jpg");
        images.add("https://i.imgur.com/Kswaw31.jpg");
        images.add("https://i.imgur.com/wErpZLa.jpg");
        images.add("https://i.imgur.com/PL1VIoc.jpg");
        images.add("https://i.imgur.com/uPzHFQO.jpg");
        images.add("https://i.imgur.com/KstCv5J.jpg");

        for (int i = 1; i <= count; i++) {
            contacts.add(new Post(titles.get(i % titles.size()), texts.get(i % texts.size()), images.get(i % images.size()), "zafig", new Date()));
        }

        return contacts;
    }

    public static ArrayList<Post> createPostsList2(int count) {
        ArrayList<Post> contacts = new ArrayList<Post>();

        ArrayList<String> texts = new ArrayList<String>();
        texts.add("eyyy this is my post");

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("eyyy this is my title");

        ArrayList<String> images = new ArrayList<String>();
        images.add("https://i.imgur.com/KstCv5J.jpg");

        for (int i = 1; i <= count; i++) {
            contacts.add(new Post(titles.get(i % titles.size()), texts.get(i % texts.size()), images.get(i % images.size()), "yourusername", new Date()));
        }

        return contacts;
    }
}