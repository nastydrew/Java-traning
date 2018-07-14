package com.gymfox.army.units;

import com.gymfox.army.ability.DefaultAbility;
public class Soldier extends Unit {
    public Soldier(String name, int healthPointLimit, int damage) {
        super(name, healthPointLimit, damage);
        this.ability = new DefaultAbility(this);
    }
}
