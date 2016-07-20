
fnValue = 50;
cournersRound = 3;
mountLength = 140;
mountHeight = 7.5;

module singleMount(y) {
    translate([0, y, -3]) {
        minkowski() {
            cube([mountLength, 12, mountHeight], true);
            sphere(cournersRound, $fn = fnValue);
            
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
        #cube([1, 1, 16], true);
    }
    
    translate([-35, 0, 0]) {
        #cube([1, 53, 1], true);
    }
}

singleSideMount(-22);
singleMount(0);
singleSideMount(22);
beams();
beamEnd(27);
beamEnd(-27);

phone();