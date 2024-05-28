class Node:
    def __init__(self, value, index):
        self.index = index
        self.value = value
        self.next = None
        self.kind_link_next = None  # 'Adjacent' or 'Breakpoint'
        self.prev = None
        self.kind_link_prev = None  # 'Adjacent' or 'Breakpoint'
        self.far_link_1 = None
        self.far_link_2 = None

    def reassign_links_and_index(self, new_prev, new_next, new_index):
        self.prev = new_prev
        self.next = new_next
        self.index = new_index

    def __str__(self):
        return f"Node {self.index}: {self.value}, prev: {self.prev.value if self.prev else None}, next: {self.next.value if self.next else None}, kind_link_next: {self.kind_link_next}, kind_link_prev: {self.kind_link_prev}, far_link_1: {self.far_link_1.value if self.far_link_1 else None}, far_link_2: {self.far_link_2.value if self.far_link_2 else None}"
    
    def copy(self):
        node = Node(self.value, self.index)
        node.next = self.next
        node.prev = self.prev
        node.kind_link_next = self.kind_link_next
        node.kind_link_prev = self.kind_link_prev
        node.far_link_1 = self.far_link_1
        node.far_link_2 = self.far_link_2
        return  node
    
    def node_print_detailed_information(self):
        print(f"Node {self.index}: {self.value}")
        if self.next:
            print(f"Next: {self.next.value} ({self.kind_link_next})")
        if self.prev:
            print(f"Prev: {self.prev.value} ({self.kind_link_prev})")
        if self.far_link_1:
            print(f"Far link 1: {self.far_link_1.value}")
        if self.far_link_2:
            print(f"Far link 2: {self.far_link_2.value}")
        print()

class PancakeSorter:
    def __init__(self, pancakes):
        self.head = None
        self.tail = None
        self.pancakes_size = len(pancakes)
        self.num_flips = 0
        self.flips_list = []
        self.create_list_inverse(pancakes)
        self.setup_links()

    def create_list_inverse(self, pancakes):
        min_val = min(pancakes) - 1
        max_val = max(pancakes) + 1
        self.head = Node(max_val, 0)
        current = self.head
        for i in range(1, len(pancakes) + 1):
            new_node = Node(pancakes[len(pancakes) - i], i)
            current.next = new_node
            new_node.prev = current
            current = new_node
        self.tail = Node(min_val, len(pancakes) + 1)
        current.next = self.tail
        self.tail.prev = current
    
    def setup_links(self):
        current = self.head
        while current:
            self.assign_breakpoints_and_adjacents_to_node(current)
            current = current.next
        
        current = self.head
        while current:
            self.assign_far_nodes_to_node(current)
            current = current.next
    
    def assign_breakpoints_and_adjacents_to_node(self, node):
        if node.next and abs(node.value - node.next.value) != 1:
            node.kind_link_next = 'Breakpoint'
        elif node.next and abs(node.value - node.next.value) == 1:
            node.kind_link_next = 'Adjacent'
            
        if node.prev and abs(node.value - node.prev.value) != 1:
            node.kind_link_prev = 'Breakpoint'
        elif node.prev and abs(node.value - node.prev.value) == 1:
            node.kind_link_prev = 'Adjacent'
        
    def assign_far_nodes_to_node(self, node):
        far_link_1 = None
        far_link_2 = None
        other = self.head
        while other:
            if other != node and abs(node.value - other.value) == 1:
                if not far_link_1 and node.next != other and node.prev != other:
                    far_link_1 = other
                elif not far_link_2 and node.next != other and node.prev != other:
                    far_link_2 = other
                elif far_link_1 and far_link_2:
                    break
            other = other.next
        node.far_link_1 = far_link_1
        node.far_link_2 = far_link_2
    
    def search_node_by_index(self, index):
        current = self.head
        while current:
            if current.index == index:
                return current
            current = current.next
        return None
    
    def flip_pancakes(self, index):
        if index == 0 or index == 1:
            return
        
        self.flips_list.append(self.pancakes_size - 1 - (index - 1))
        
        index_node = self.search_node_by_index(index)
        index_old_prev, index_old_next = index_node.prev, index_node.next
        
        index_node.reassign_links_and_index(new_prev=self.head, new_next=index_old_prev, new_index=1)
        self.head.next = index_node
        
        self.assign_breakpoints_and_adjacents_to_node(self.head)
        self.assign_breakpoints_and_adjacents_to_node(self.head.next)
        
        i = 2
        final_node_in_index = None
        current = index_node.next
        while i <= index and current:
            new_prev, new_next = None, None
            if i != index:
                new_prev = current.next
                new_next = current.prev
            else:
                new_prev = current.next
                new_next = index_old_next
                final_node_in_index = current
                
            current.reassign_links_and_index(new_prev=new_prev, new_next=new_next, new_index=i)
            self.assign_breakpoints_and_adjacents_to_node(current)
            current = current.next
            i += 1
        
        index_old_next.prev = final_node_in_index
        self.assign_breakpoints_and_adjacents_to_node(index_old_next)
        
        current = self.head
        while current:
            if current.index in [0, 1, index, index + 1]:
                self.assign_far_nodes_to_node(current)
            current = current.next
    
        self.num_flips += 1
    
    def identify_good_far_links(self):
        current = self.head
        while current:
            far_link_1 = current.far_link_1
            far_link_2 = current.far_link_2
            
            if not far_link_1:
                current = current.next
                continue
            
            else:
                type_link = self.verify_far_link_type(current, far_link_1)
                if type_link is not None:
                    return current, 'far_link_1', type_link
                
                elif far_link_2:
                    type_link = self.verify_far_link_type(current, far_link_2)
                    if type_link is not None:
                        return current, 'far_link_2', type_link
                
            current = current.next
        
        return None, None, None
    
    def verify_far_link_type(self, current_node, far_node): 
        if current_node.index == 1 and current_node.kind_link_prev == 'Breakpoint' and far_node.kind_link_prev == 'Breakpoint':
            return 'type_1'
        elif current_node.index != 0 and current_node.kind_link_next == 'Breakpoint' and far_node.kind_link_next == 'Breakpoint' and far_node.index != 0:
            return 'type_2'
        elif current_node.index != 0 and current_node.kind_link_next == 'Breakpoint' and far_node.kind_link_prev == 'Breakpoint':
            return 'type_3'
        
        return None
    
    def delete_good_far_link(self, node, num_far_link, type_good_far_link):    
        node_low = node
        if num_far_link == 'far_link_1':
            node_high = node.far_link_1
        else:
            node_high = node.far_link_2
            
        if node_low.index > node_high.index:
            node_low, node_high = node_high, node_low
                
        if type_good_far_link == 'type_1':
            self.flip_pancakes(index=node_high.index - 1)
        elif type_good_far_link == 'type_2':
            self.flip_pancakes(index=node_high.index)
            self.flip_pancakes(index=node_low.index - 1)
        elif type_good_far_link == 'type_3':
            if node_low.index == 0:
                self.flip_pancakes(index=node_high.index)
            else: 
                self.flip_pancakes(index=node_low.index)
                self.flip_pancakes(index=node_high.index - 1)
    
    def flip_pancakes_while_good_far_links(self):
        while True:
            node, num_far_link, type_good_far_link = self.identify_good_far_links()
            if not node:
                break
            self.delete_good_far_link(node, num_far_link, type_good_far_link)
        return self.num_flips

    def print_list(self):
        current = self.tail.prev
        while current.prev:
            print(f"{current.value}", end=" <-> ")
            current = current.prev
        print(f"{current.value}")
    
    def get_flips_list(self):
        return ' '.join(map(str, self.flips_list))

def process_input_file(input_file, output_file):
    with open(input_file, 'r') as infile, open(output_file, 'w') as outfile:
        num_cases = int(infile.readline().strip())
        for _ in range(num_cases):
            pancakes = list(map(int, infile.readline().strip().split()))
            sorter = PancakeSorter(pancakes)
            num_flips = sorter.flip_pancakes_while_good_far_links()
            flips_list = sorter.get_flips_list()
            print (num_flips)
            if (num_flips == 1 and flips_list == "0"):
                outfile.write("ORDENADO\n")
            elif (num_flips == 0):
                outfile.write("0\n")
            else:
                outfile.write(f"{flips_list} 0\n")
    print("SE HA ESCRITO EN EL P3.OUT")


# Especificar los nombres de los archivos de entrada y salida
input_file = 'P3.in'
output_file = 'P3.out'
process_input_file(input_file, output_file)
