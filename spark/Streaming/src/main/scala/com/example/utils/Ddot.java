package com.example.utils;

public final class Ddot {
    public Ddot() {
    }

    public static double ddot(int var0, double[] var1, int var2, int var3, double[] var4, int var5, int var6) {
        double var7 = 0.0D;
        boolean var9 = false;
        boolean var10 = false;
        boolean var11 = false;
        boolean var12 = false;
        boolean var13 = false;
        double var16 = 0.0D;
        var16 = 0.0D;
        var7 = 0.0D;
        if (var0 <= 0) {
            return var16;
        } else {
            int var18;
            int var19;
            if (var3 == 1 && var6 == 1) {
                int var22 = var0 % 5;
                if (var22 != 0) {
                    var19 = 1;

                    for(var18 = var22 - 1 + 1; var18 > 0; --var18) {
                        var7 += var1[var19 - 1 + var2] * var4[var19 - 1 + var5];
                        ++var19;
                    }

                    if (var0 < 5) {
                        return var7;
                    }
                }

                int var23 = var22 + 1;
                var19 = var23;

                for(var18 = (var0 - var23 + 5) / 5; var18 > 0; --var18) {
                    var7 = var7 + var1[var19 - 1 + var2] * var4[var19 - 1 + var5] + var1[var19 + 1 - 1 + var2] * var4[var19 + 1 - 1 + var5] + var1[var19 + 2 - 1 + var2] * var4[var19 + 2 - 1 + var5] + var1[var19 + 3 - 1 + var2] * var4[var19 + 3 - 1 + var5] + var1[var19 + 4 - 1 + var2] * var4[var19 + 4 - 1 + var5];
                    var19 += 5;
                }

                return var7;
            } else {
                int var20 = 1;
                int var21 = 1;
                if (var3 < 0) {
                    var20 = (-var0 + 1) * var3 + 1;
                }

                if (var6 < 0) {
                    var21 = (-var0 + 1) * var6 + 1;
                }

                var19 = 1;

                for(var18 = var0 - 1 + 1; var18 > 0; --var18) {
                    var7 += var1[var20 - 1 + var2] * var4[var21 - 1 + var5];
                    var20 += var3;
                    var21 += var6;
                    ++var19;
                }

                return var7;
            }
        }
    }
}