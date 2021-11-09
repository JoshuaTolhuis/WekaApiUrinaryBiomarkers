if(!require(RWeka)) install.packages("RWeka",repos = "http://cran.us.r-project.org")

library(RWeka)

args = commandArgs(trailingOnly = TRUE)
file.name <- args[1]

Pancreatic_data <- read.csv(file.name, header = T, sep = ",")

row.has.na <- apply(Pancreatic_data[1:13], 1, function(x){any(is.na(x))})
Pancreatic_data <- Pancreatic_data[!row.has.na,]

Pancreatic_data$sample_id <- NULL
Pancreatic_data$patient_cohort <- NULL
Pancreatic_data$sample_origin <- NULL
Pancreatic_data$age <- NULL
Pancreatic_data$sex <- NULL
Pancreatic_data$benign_sample_diagnosis <- NULL
Pancreatic_data$stage <- NULL
Pancreatic_data$REG1A <- NULL

Pancreatic_data$diagnosis <- as.character(Pancreatic_data$diagnosis)

write.arff(Antibody_data, file = "weka_data.arff")
