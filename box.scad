profile = 0;

// sizes in mm
width = 80;
height = 60;
depth = 30; 
thickness = 3;
coverThickness = 2;
coverDepth = 1;
coverOffset = 0;
holeRadius = 2;

mountWidth = 14;
mountHeigth = 10;
mounthDepth = 5;

controlsOffset = 20;
ledRadius = 2.5;
ledXPosition = controlsOffset;
switchRadius = 3;
switchXPosition = width - controlsOffset;

holeHeigth = thickness;
bottomHoleOffset = depth - thickness;
holePosition = 2 * thickness - 1;
widthInside = width - (2 * thickness);
heightInside = height - (2 * thickness);

module box() {
    difference() {
        cube([width, height, depth], false);
        translate([thickness, thickness, -thickness]) {
            cube([widthInside, heightInside, depth], false);
        }
    }
}

module hole(x, y, z) {
    translate([x, y, z]) {
        cylinder(holeHeigth, holeRadius, holeRadius, false);
    }
}

module holes(bottomOffset) {
    hole(holePosition, holePosition, bottomOffset);
    hole(holePosition, heightInside + 1, bottomOffset);
    hole(widthInside + 1, holePosition, bottomOffset);
    hole(widthInside + 1, heightInside + 1, bottomOffset);
}

module holeBox(x, y) {
    translate([x, y, 1]) {
        difference() {
            cube([8, 8, depth - 1 - thickness]);
            translate([2, 2, 0]) {
                cube([4, 4, depth - 1 - thickness]);
            }
        }
    }
}

module holeBoxes() {
    holeBox(1, 1);
    holeBox(1, heightInside - thickness);
    holeBox(widthInside - thickness, 1);
    holeBox(widthInside - thickness, heightInside - thickness);
}

module cover() {
    translate([0, 0, coverOffset]) {
        cube([width, height, coverThickness], false);
        translate([thickness, thickness, coverThickness]) {
            cube([widthInside, heightInside, coverDepth], false);
        }
    }
}

module ledHoles() {
    halfDepth = depth / 2;
    translate([ledXPosition, thickness + 1, halfDepth]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, ledRadius, ledRadius, false);
            translate([15, 0 ,0]) {
                cylinder(heigth, ledRadius, ledRadius, false);
            }
        }
    }
}

module switchHole() {
    halfDepth = depth / 2;
    translate([switchXPosition, thickness + 1, halfDepth]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, switchRadius, switchRadius, false);
        }
    }
}

module mount(x) {
    translate([x, (height - mountWidth) / 2, -(coverOffset + mountHeigth - 1)]) {
        difference() {
            cube([mounthDepth, mountWidth, mountHeigth], false);
            translate([0, (mountWidth - 4) / 2, 3]) {
                cube([mounthDepth, 4, 3], false);
            }            
        }
    }
}

module verticalMount() {
    translate([(width - mounthDepth) / 2, height - 1, 16]) {
            difference() {
            cube([mounthDepth, mountHeigth, mountWidth], false);
            translate([0, 4, (mountWidth - 4) / 2]) {
                cube([mounthDepth, 3, 4], false);
            }
        }
    }
}

module model() {
    difference() {
        box();
        holes(bottomHoleOffset);
        ledHoles();
        switchHole();
    }
    
    holeBoxes();
    verticalMount();
    
    translation = profile == 1 
        ? [0, 0, -4] 
        : [0, 72, depth - thickness];
    
    translate(translation) {
        difference() {
            cover();
            holes(coverOffset);
        }
        mount(18);
        mount(57);
    }
}

rotate([0, 180, 0]) {
    translate([-width/2, -height/2, -depth]) {
        difference() {
            model();
            if (profile == 1) {
                translate([0, height / 2, -30]) {
                    cube([width / 2, height, 60], false);
                }
            }
        }
    }
}