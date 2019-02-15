# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from dateutil.parser import parse


def create_for_star(star):
    return '''
### [{star[name]}]({star[url]})
{star[desc]}
- stars:{star[stars]}
- update time:{star[update_time]}

'''.format(star=star)


class GithubPipeline(object):
    stars = []

    def process_item(self, item, spider):
        item['name'] = item['name'][1:]
        item['update_time'] = parse(item['update_time']).strftime("%Y-%m-%d %H:%M:%m")
        self.stars.append(item)
        # print(item)
        return item

    def close_spider(self, spider):
        self.stars.sort(key=key_funcs, reverse=True)
        with open('achievec_stars.md', 'w') as outfile:
            # json.dump(self.stars, outfile)
            for star in self.stars:
                outfile.write(create_for_star(star))
        # print(self.stars)


def key_funcs(e):
    return e['stars']
