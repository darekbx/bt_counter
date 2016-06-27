// sizes in mm
width = 80;
height = 60;
depth = 30; 
thickness = 3;
coverThickness = 2;
coverDepth = 1;
coverOffset = -20;
holeRadius = 2;

controlsOffset = 20;
ledRadius = 2.5;
ledXPosition = controlsOffset;
switchRadius = 3;
switchXPosition = width - controlsOffset;

holeHeigth = thickness;
bottomHoleOffset = depth - thickness;
holePosition = 2 * thickness;
widthInside = width - (2 * thickness);
heightInside = height - (2 * thickness);

module box() {
    difference() {
        cube([width, height, depth], false);
        translate([thickness, thickness, -thickness]) 
            cube([widthInside, heightInside, depth], false);
    }
}

module hole(x, y, z) {
    translate([x, y, z])
        cylinder(holeHeigth, holeRadius, holeRadius, false);
}

module holes(bottomOffset) {
    hole(holePosition, holePosition, bottomOffset);
    hole(holePosition, heightInside, bottomOffset);
    hole(widthInside, holePosition, bottomOffset);
    hole(widthInside, heightInside, bottomOffset);
}

module cover() {
    translate([0, 0, coverOffset]) {
        cube([width, height, coverThickness], false);
        translate([thickness, thickness, coverThickness])
            cube([widthInside, heightInside, coverDepth], false);
    }
}

module ledHoles() {
    translate([ledXPosition, thickness + 1, depth / 2]) {
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
    translate([switchXPosition, thickness + 1, depth / 2]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, switchRadius, switchRadius, false);
        }
    }
}

translate([-width/2, -height/2, -depth]) {
    difference() {
        box();
        holes(bottomHoleOffset);
        ledHoles();
        switchHole();
    }
    
    difference() {
        cover();
        holes(coverOffset);
    }
}