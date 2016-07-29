
fnValue = 50;
cournersRound = 2;
mountLength = 140;
mountHeight = 7.5;
handlebarDiameter = 26;

module singleMount(y) {
    translate([0, y, -3]) {
        difference() {
            minkowski() {
                cube([mountLength, 14, mountHeight], true);
                sphere(cournersRound, $fn = fnValue);
            }
            ladderMount();
        }
    }
}

module singleSideMount(y) {
    translate([20, y, 0]) {
        minkowski() {
            cube([mountLength - 40, 3, 2], true);
            sphere(cournersRound, $fn = fnValue);
        }
    }
}

module singleBeam(x) {
    translate([x, 0, 0]) {
        minkowski() {
            cube([1, 44, 1], true);
            sphere(2, $fn = fnValue);
        }
    }
}

module beamEnd(x) {
    translate([69.5, x, 0]) {
        minkowski() {
            cube([1, 10, 2], true);
            sphere(cournersRound, $fn = fnValue);
        }
    }
}

module beams() {
    for (a =[0:3]) {
        singleBeam(65 - (a * 30));
    }  
}

module phone() {
    translate([20, 0, 14]) {
        %cube([132, 67, 13], true);
    }
    
    translate([-35, 0, -3]) {
        %cube([1, 1, 16], true);
    }
    
    translate([-35, 0, 0]) {
        %cube([1, 53, 1], true);
    }
}

module mount() {
    translate([-90, 0, -3]) {
        difference() {
            sphere(22, $fn = fnValue);
            mountDiff();
        }
        
        translate([15, -9, 2.2]) {
            rotate([0, 14, 0]) { 
                cube([25, 18, 8]);
            }
        }
        
    }    
}

module mountDiff() {
    translate([-2.5, -35, -35]){
        cube([5, 70, 70]);
    }
    rotate([90, 90, 0]) {
        translate([0, 0, -50]) {
            cylinder(100, 13, 13, $fn = fnValue);
        }
    }
    translate([-25, 9, -25]) {
        cube([50, 20, 50]);
    }
    translate([-25, -29, -25]) {
        cube([50, 20, 50]);
    }
    
    screwHole(-16);
    screwHole(16);
}

module screwHole(y) {
    translate([-25, 0, y]) {
        rotate([90, 0, 90]) {
            cylinder(50, 2.5, 2.5, $fn = fnValue);
            translate([0, 0, -5]) {
                cylinder(20, 6, 6, $fn = fnValue);
            }
            translate([0, 0, 35]) {
                cylinder(20, 6, 6, $fn = fnValue);
            }
        }
    }
}

module ladder() {
    translate([0, -60, 1]) {
        singleSideMount(-22);
        beams();
        beamEnd(27);
        beamEnd(-27);
        singleSideMount(22);
    }
}

module ladderMount() {
    translate([0, 0, 4.5]) {
        for (a =[0:3]) {
            translate([65 - (a * 30), 0, 0]) {
                minkowski() {
                    cube([1.25, 44, 1], true);
                    sphere(2, $fn = fnValue);
                }
            }
        } 
     }
}

module model() render() { 
    singleMount(0);
    ladder();
    
    mount();
}

model();
//phone();