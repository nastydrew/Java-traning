package com.gymfox.army.magicskills;

public final class MagicSkills {
    private double battleMagicSkill;
    private double healingMagicSkill;

    public MagicSkills(double battleMagicSkill, double healingMagicSkill) {
        this.battleMagicSkill = battleMagicSkill;
        this.healingMagicSkill = healingMagicSkill;
    }

    public double getBattleMagicSkill() {
        return battleMagicSkill;
    }

    public double getHealingMagicSkill() {
        return healingMagicSkill;
    }
}
