/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.develgame.techdemo.player;

import com.jme3.scene.Spatial;

/**
 *
 * @author izemskov
 */
public class TimedBullet {
    private final Spatial bullet;
    private final float maxTime;
    private float time;

    public TimedBullet(Spatial bullet, float maxTime) {
        this.bullet = bullet;
        this.maxTime = maxTime;
    }

    public Spatial getSpatial() {
        return bullet;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public float getTime() {
        return time;
    }

    public float updateTime(float tpf) {
        time += tpf;
        return time;
    }
}
