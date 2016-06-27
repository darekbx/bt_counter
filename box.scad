translate([0, 0, 0]) {
    difference() {
        color([0.4, 0.4, 0.4]) {
            cube([76, 60, 28], false);
        }
        translate([3, 3, -3]) 
            cube([70, 54, 28], false);
        
    }
    
    difference() {
        translate([0, 0, -10])
            cube([76, 60, 3], false);
        
        translate([5, 5, -10])
            cylinder(8, 2, 2, false);
        
        translate([5, 56, -10])
            cylinder(8, 2, 2, false);
    }
    
}