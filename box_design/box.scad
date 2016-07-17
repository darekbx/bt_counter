include <text_generator.scad>;

profile = 0;
showComponents = 1;
fnValue = 20;

// sizes in mm
width = 74;
height = 76;
depth = 30; 
thickness = 3;
coverThickness = 2;
coverDepth = 1;
coverOffset = 0;
holeRadius = 2;
textDepth = 0.75;

mountWidth = 14;
mountHeigth = 6;
mounthDepth = 5;

batteryHolderWidth = 55;
batteryHolderSize = 25;

controlsOffset = 20;
ledRadius = 4;//2.5;
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

module hole(x, y, z, cutHole) {
    translate([x, y, z]) {
        translate([0, 0, -0.5]) {
            cylinder(holeHeigth + 1, holeRadius, holeRadius,$fn=fnValue, false);
        }
        if (cutHole) {
            translate([-holeRadius, -holeRadius, 2]) {
                cube([holeRadius * 2, holeRadius * 2, 1], false);
            }
        }
    }
}

module holes(bottomOffset, cutHole) {
    hole(holePosition, holePosition, bottomOffset, cutHole);
    hole(holePosition, heightInside + 1, bottomOffset, cutHole);
    hole(widthInside + 1, holePosition, bottomOffset, cutHole);
    hole(widthInside + 1, heightInside + 1, bottomOffset, cutHole);
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
        translate([thickness + 0.25, thickness + 0.25, coverThickness]) {
            cube([widthInside - 0.5, heightInside - 0.5, coverDepth], false);
        }
    }
}

module ledHoles() {
    halfDepth = depth / 2;
    translate([ledXPosition, thickness + 1, halfDepth]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, ledRadius, ledRadius,$fn=fnValue, false);
        }
    }
}

module switchHole() {
    halfDepth = depth / 2;
    translate([switchXPosition, thickness + 1, halfDepth]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, switchRadius, switchRadius,$fn=fnValue, false);
        }
    }
}

module mount(x) {
    translate([(width - mountWidth) / 2, x, -(coverOffset + mountHeigth - 1)]) {
        difference() {
            cube([mountWidth, mounthDepth, mountHeigth], false);
            translate([(mountWidth - 4) / 2, 0, 3]) {
                cube([4, mounthDepth, 2], false);
            }
        }
    }
}

module verticalMount(y) {
    translate([width - 1, y, depth - mounthDepth]) {
        difference() {
            cube([mountHeigth, mountWidth, mounthDepth], false);
            translate([1, (mountWidth - 4) / 2, 0]) {
                cube([2, 4, mounthDepth], false);
            }
        }
    }
}

module logo() {
    rotate([90, 180, -90]) {
        translate([48, -19, -textDepth]) {
            scale([1,1,1]) {
                drawtext("BTC");
            }
        }
    }
}

module sensorHoles() {
    holeSize = 3;
    holeDistance = 30;
    translate([width - 4, holeDistance - holeSize/2, 0]) {
        cube([5, holeSize, 5], false);
    }
    translate([thickness, holeDistance, 5]) {
        rotate([0, 90, 0]) {
            cylinder(width, holeSize/2, holeSize/2,$fn=fnValue, false);
        }
    }
}

module batteryHolder() {
    translate([2, height - 32, depth - 22]) {
        cube([width - 4, 3, 20]);
    }  
}

module components() {
    // battery box 55x25x25
    translate([9.5, height - 28.5, 1]) {
        cube([batteryHolderWidth, batteryHolderSize, batteryHolderSize]);
    }
    // bluno 5x30x34
    translate([30, 10, 24]) {
        cube([34, 30, 5]);
    }
}

module voltmeter() {
    // voltmeter 25x12x8
    translate([-1, 15, 10]) {
        #cube([7, 23, 10.5]);
    }
}

module model() render() {
    difference() {
        box();
        holes(bottomHoleOffset, false);
        ledHoles();
        switchHole();
        sensorHoles();
        logo();
        voltmeter();
    }
    
    holeBoxes();
    verticalMount(0);
    verticalMount(height - mountWidth);
    batteryHolder();
    
    translation = profile == 1 
        ? [0, 0, -3] 
        : [0, 78, depth - thickness];
    
    translate(translation) {
        difference() {
            cover();
            holes(coverOffset, true);
        }
        mount(24);
        mount(49);
    }
    
    if (showComponents == 1) {
        #components();
    }
}

rotate([0, 180, 0]) {
    translate([-width/2, -height/2, -depth]) {
        difference() {
            model();
            if (profile == 1) {
                translate([0, height / 2, -(depth - 10)]) {
                    cube([width / 2, height, 60], false);
                }
            }
        }
    }
}