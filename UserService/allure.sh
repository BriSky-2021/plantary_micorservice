cp -r allure-report/history allure-results
allure generate -c
# allure open allure-report
cp -r ./allure-report ../docs
