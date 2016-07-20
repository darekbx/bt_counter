
fnValue = 50;
cournersRound = 3;
mountLength = 130;
mountSize = 20;

module singleMount(y) {
    translate([0, y, 0]) {
        
        minkowski() {
            cube([140, 12, 7], true);
            sphere(cournersRound, $fn = fnValue);
        }

    }
}

module phone() {
    translate([0, 0, 14]) {
        #cube([132, 67, 13], true);
    }
}

singleMount(20);
singleMount(-20);
phone();